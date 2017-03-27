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

## loptop file
#TTN_af <- read.table("D:/PhD/QualifyTTN_variants_OnExons.txt", header = T, sep = ",")

TTN_af <- TTN_af$Allele.Frequency

variants.count <- length( TTN_af )

# head(TTN_af)

###############################
## 
## Step two
## Pass the allele frequencies we got in step-one to a function simulateGenotypes()
## Return n1 (the counts of heterozygous variants), n2 (the counts of homozygous variants)
##        Pai2g (the sum of Pai2g * Rho) from Equation #8 in the proposal
## 

simulateGenocypes<-function(af.list){
  
  len <- length(af.list)   # the length of allele frequencies vector
  af.sim1 <- runif(n=len, min=0, max=1)               #generate a vector of random double numbers [0,1]
  af.sim2 <- runif(n=len, min=0, max=1)
  
  ## initialize a vector of zeros to store the simulated allele frequencies
  aflist.sim <- rep(0, len)
  
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
      
      aflist.sim[i] <- aflist.sim[i] + 1
    } 
    
    if( af.list[i] > af.sim1[i]){
      n1 <- n1 + 1
      
      aflist.sim <- aflist.sim[i] + 1
    }
    
    
  } # end for i in 1:len loop; 
  
  ## update aflist.sim from counts to frequencies by /(len * 2)
  aflist.sim <- aflist.sim 
  
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
  
  if(n1 > 1)
    print( c(n1, n2, Pai2g))
  #print( length( c(aflist.sim, Pai2g)))
  
  ## return a list of variants count on each know allele site, plus the calculated Pai2g for this 'person'
  return( c(aflist.sim, Pai2g) )
  
}


##################################################
##
## step 2.5
## Calculate the Sum( Pai2g * Rho) for the simulated genotypes
## 
CalPai2gRho <- function( matrix ){
  
  row <- dim( matrix)[1]
  col <- dim( matrix)[2] 
  
  ## calculate the allele frequencies from the allele count matrix
  af.list <- rep(0, col)
  
  for( i in 1:col){
    
    af.list[i] <- sum( matrix[ ,i] ) / (row * 2)
  }
  
  Pai2gRho <- 1
  for(i in 1:col){
    
    Pai2gRho <- Pai2gRho * ( 1 - af.list[i])
  }
  
  Pai2gRho <- ( 1 - Pai2gRho )^2
  
  return(Pai2gRho) 
} 


#####################################
##
## step three
## 
## Simulate genotypes for 100k times, get n1, n2, Pai2g Matrix
## Perform Rao's Score Test on this set of data simulated
## Print the Score and P-value
## 

simu100kGenotypes <- function(TTN_af, sample.size, variants.count){
  sim.list <- simulateGenocypes(TTN_af)
  print(c('sim.length', length(sim.list)) )
  
  sim.matrix <- matrix( sim.list, nrow = 1, ncol = length(sim.list), byrow = T)
  
  for(i in 2:sample.size)
    sim.matrix <- rbind(sim.matrix, simulateGenocypes(TTN_af))
    
#    sim.list <- c(sim.list, simulateGenocypes(TTN_af) )
  
#  sim.matrix <- matrix( sim.list, nrow = sample.size, ncol = variants.count+1, byrow = TRUE)

  ## the Pai2gRho for LoF variants pai2g_expected, the very last column from the matrix
  col.last <- ncol(sim.matrix)
  TTN_pai2g.sim <- sim.matrix[ ,col.last]
  sim.matrix <- sim.matrix[ ,-col.last]
  
  ttn_pai2g_exp <- 3.6328594930727866E-5  
  
  ttn_pai2g_exp <- CalPai2gRho( sim.matrix )
  
  Si.sim <- TTN_pai2g.sim - ttn_pai2g_exp
  
  n.sim <- length(Si.sim)

  ## Score.sim = ( sum(Si.sim))^2 / ( n.sim * var(Si.sim) )
  I_beta <- TTN_pai2g.sim^2 - ttn_pai2g_exp^2

  Score.sim <- ( sum(Si.sim))^2 / (sum(I_beta) )
  
  ## Calculate p-values 
  p.value <- (1 - pchisq(Score.sim, df=1)) 
  
  print(c('Pai2gRho', ttn_pai2g_exp, 'Pvalue:', p.value))
  return( p.value )
}


 

###################################################
##
## step four
## Perform the simulation for 1000 times, see the PValues returned
## 

PValues <- NULL
sample.size <- 100000

## Try parellel 
library(foreach)
library(doMC)
registerDoMC(16)

## when doing parallel, have to use list<-foreach() to catch all the returned values from all loops!
list <- foreach( i = 1:100) %dopar% {

  print(c('simulating: ', i))
  PValues <- c(PValues, simu100kGenotypes(TTN_af, sample.size, variants.count))
  
}

PValues <- c(PValues, unlist(list) )


#############
## non parallel
#for(i in 1:200){
#  print(i)
#  PValues <- c(PValues, simu100kGenotypes(TTN_af, sample.size, variants.count))
  
#}

print(PValues)

mean(PValues < 0.05)

pdf(file = "histPvalues0326.pdf")
 hist(PValues)
dev.off()

mean(PValues < 0.05) 

## hist(PValues)

## mean(PValues < 0.05)
###################################################

