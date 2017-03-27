################################
##
## 03-25-2017
## Viability Simulate
################################
## 
## Step one
## Read in a vector of allele frequencies.
## The af list was pulled down from ExAC, filtered by CCDS.r14, rare threhold set at 10%
## 

TTN_af <- read.table("/work/AndrewGroup/ViabilitySimulation/QualifyTTN_variants_OnExons.txt", header = T, sep = ",")
TTN_af <- read.table("D:/PhD/QualifyTTN_variants_OnExons.txt", header = T, sep = ",")
TTN_af <- TTN_af$Allele.Frequency

# head(TTN_af)

###############################
## 
## Step two
## Pass the allele frequencies we got in step-one to a function simulateGenotypes()
## Return n1 (the counts of heterozygous variants), n2 (the counts of homozygous variants)
##        Pai2g (the sum of Pai2g * Rho) from Equation #8 in the proposal
## 

simulateGenocypes<-function(af.list, seed){
  
  set.seed(seed^2)

  len <- length(af.list)   # the length of allele frequencies vector
  af.sim1 <- runif(n=len, min=0, max=1)               #generate a vector of random double numbers [0,1]
  af.sim2 <- runif(n=len, min=0, max=1)
  
  n1 <- 0                     ## counts of heterozygous variants
  n2 <- 0                     ## counts of homozygous variants
  
  for(i in 1:len){
    
    ### get counts for the homozygous variants
    if(af.list[i] > af.sim1[i] & af.list[i] > af.sim2[i]){
      
      n2 <- n2 + 1
    } 
    
    ### get counts for the heterozygous
    if(af.list[i] > af.sim2[i]){
      n1 <- n1 + 1
      
    } 
    
    if( af.list[i] > af.sim1[i]){
      n1 <- n1 + 1
    }
    
    
  } # end for i in 1:len loop; 
  
  Pai2g <- 0
  
  if(n2 > 0){
    Pai2g <- 1
  
  } else {
      
      if( n1 < 2) {
        Pai2g <- 0
      
      } else {
        
        Pai2g <- 1 - (0.5)^(n1 - 1)  
        
        }
    
  }
  
  ##
  if(n1 > 1)
    print( c(n1, n2, Pai2g))
  
  return( Pai2g )
  
}


#####################################
##
## step three
## 
## Simulate genotypes for 100k times, get n1, n2, Pai2g Matrix
## Perform Rao's Score Test on this set of data simulated
## Print the Score and P-value
## 

simu100kGenotypes <- function(TTN_af, sample.size, seed){
  pai2g.list <- NULL

  for(i in 1:sample.size)
    pai2g.list <- c(pai2g.list, simulateGenocypes(TTN_af, seed) )
  
  TTN_pai2g.sim <- pai2g.list
  print(  summary( TTN_pai2g.sim ) )

  ttn_pai2g_exp <- 3.6328594930727866E-5  

  len <- length(TTN_pai2g_sim)

  Si.sim <- TTN_pai2g_sim - ttn_pai2g_exp 
  
  n.sim <- length(Si.sim)

  ## Score.sim = ( sum(Si.sim))^2 / ( n.sim * var(Si.sim) )
  I_beta <- TTN_pai2g_sim^2 - ttn_pai2g_exp^2

  Score.sim <- ( sum(Si.sim))^2 / (sum(I_beta) )
  
  ## Calculate p-values 
  p.value.sim <- (1 - pchisq(Score.sim, df=1)) 
  
  print(c('Pvalue: ', p.value.sim))
  return( p.value.sim )
}


###################################################
##
## step four
## Perform the simulation for 1000 times, see the PValues returned
## 

PValues <- NULL
sample.size <- 100000

## Try parellel 
install.packages("foreach")
install.packages("doMC")

library(foreach)
library(doMC)
registerDoMC(4)

foreach( i = 1:10) %dopar% {
  
  PValues <- c(PValues, simu100kGenotypes(TTN_af, sample.size))
  print(c('simulating: ', i))
}

for(i in 1:10) {
  
  seed <- 100 + i
  PValues <- c(PValues, simu100kGenotypes(TTN_af, sample.size, seed))
}

png(file = "histPvalues.png")
 hist(PValues)
dev.off()

mean(PValues < 0.05) 

## hist(PValues)

## mean(PValues < 0.05)
###################################################

