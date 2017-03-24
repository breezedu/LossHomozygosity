#################################################
##
## simulated TTN Pai2g through Rao's Score Test
RaosScoreBeta0 <- function(circle){
  
  ## 50k groups
  ## routine <- paste("D:/PhD/PhD/beta0/simulated_n2n1_", circle, ".txt", sep = "")
  
  ## 10k groups
  routine <- paste("D:/PhD/PhD/100kbeta0/simulated_n2n1_", circle, ".txt", sep = "")
  
  TTN_pai2g_sim <- read.table(routine, header = T, sep = "\t")
  TTN_pai2g_sim <- TTN_pai2g_sim $ Pai2g
  
  # summary(TTN_pai2g_sim)
  
  ## the Pai2gRho for LoF + missence variants
  #ttn_pai2g_exp <- 1.977227008827069E-4
  
  ## the Pai2gRho for LoF variants pai2g_expected
  ttn_pai2g_exp <- 3.6328594930727866E-5  
  
  len <- length(TTN_pai2g_sim)
  
  ttn_pai2g_exp <- TTN_pai2g_sim[len]
  TTN_pai2g_sim <- TTN_pai2g_sim[1:len-1]
  
  
  Si.sim <- TTN_pai2g_sim - ttn_pai2g_exp
  
  n.sim <- length(Si.sim)
  
  ## Score.sim = ( sum(Si.sim))^2 / ( n.sim * var(Si.sim) )
  I_beta <- TTN_pai2g_sim^2 - ttn_pai2g_exp^2
  
  Score.sim <- ( sum(Si.sim))^2 / (sum(I_beta) )
  
  ## Calculate p-values 
  p.value <- (1 - pchisq(Score.sim, df=1)) 
  
  print( c('Score: ', Score.sim, ' P-value: ', p.value, ' Pai2gRho: ', ttn_pai2g_exp) )
  return( p.value ) 
}
########################

## calculate P-Values for 226 groups of simulated genotypes with 50K individuals in each group.
## initial PValues.rao as a null vector
PValues.rao.b0 <- NULL

## read in 226 * 50,000 Pai2|g, apply to Rao's Score Test formula, calculate the P-values

for(i in 0:629){
  
  PValues.rao.b0 <- c(PValues.rao.b0, RaosScoreBeta0(i))
}

hist(PValues.rao.b0, breaks = 40 )
mean( PValues.rao.b0 < 0.05)


## density of p-values from Rao's Score Test
density(PValues.rao.b0) 

## plot P-values from Rao's Score Test
plot(density(PValues.rao.b0), main = 'Plot 2000 Samples P-Values when beta=0')

opr <- par(lwd=3)

hist(PValues.rao.b0, 
     lwd=2,
     breaks = 20,
     main = 'Histogram of PValues under Beta=0', 
     xlim = c(0,1),
     #col = "blue",
     xlab = paste("P-vlues,", length(PValues.rao.b0), " samples"),
     ylab = "Density")
par(opr)

mean( PValues.rao.b0 < 0.05)

mean( PValues.rao.b0 > 0.95)

summary(PValues.rao.b0)

hist(PValues.rao.b0, 
     breaks=seq(0,1,l=20),
     freq=FALSE,
     col="blue",
     main="Histogram of PValues under Beta=0",
     xlab="P-values",
     ylab="sum",
     yaxs="i",
     xaxs="i")


sum(PValues.rao.b0 < 0.05)
sum(PValues.rao.b0 < 0.025)


## install.packages('car')
library(car)

## plot the qq uniform fit
qqPlot(PValues.rao.b0, distribution="unif",
       main = "qqPlot of uniform distribution")




######################################################################
## check all Pai2g*Rho simulated
######################################################################

CheckPai2gRho <- function(circle){
  
  ## 50k groups
  ## routine <- paste("D:/PhD/PhD/beta0/simulated_n2n1_", circle, ".txt", sep = "")
  
  ## 10k groups
  routine <- paste("D:/PhD/PhD/100kbeta0/simulated_n2n1_", circle, ".txt", sep = "")
  
  TTN_pai2g_sim <- read.table(routine, header = T, sep = "\t")
  TTN_pai2g_sim <- TTN_pai2g_sim $ Pai2g
  
  # summary(TTN_pai2g_sim)
  
  ## the Pai2gRho for LoF + missence variants
  #ttn_pai2g_exp <- 1.977227008827069E-4
  
  ## the Pai2gRho for LoF variants pai2g_expected
  ttn_pai2g_exp <- 3.6328594930727866E-5  
  
  len <- length(TTN_pai2g_sim)
  
  ttn_pai2g_exp <- TTN_pai2g_sim[len]

  # print( c('Score: ', Score.sim, ' P-value: ', p.value, ' Pai2gRho: ', ttn_pai2g_exp) )
  return( ttn_pai2g_exp ) 
}
########################

pai2g.sim <- NULL

for(i in 0:479){
  
  pai2g.sim <- c(pai2g.sim, CheckPai2gRho(i))
}

plot(pai2g.sim)
abline(h = 3.6328594930727866E-5, col='blue')


hist(pai2g.sim ,
     main = 'Hist of Sum(Pai2g*Rho) simulated') 


################################################################################################
mean(PValues.rao < 0.05)

## density of p-values from Rao's Score Test
density(PValues.rao) 

## plot P-values from Rao's Score Test
plot(density(PValues.rao), main = 'Plot 1000 groups P-Values, sample size 10k')


for(i in 101:229){
  
  PValues.rao <- c(PValues.rao, RaosScore(i))
}

## density of p-values from Rao's Score Test
density(PValues.rao) 

## plot P-values from Rao's Score Test
plot(density(PValues.rao), main = 'Plot 229 groups P-Values')

summary(PValues.rao)





##################################################################
## update p-values simutanously

retC <- NULL

Si.sim <- NULL
P.values <- NULL

for(circle in 125:207){
  
  ## 10k groups
  routine <- paste("D:/PhD/PhD/simulated_n2n1_", circle, ".txt", sep = "")
  
  TTN_pai2g_sim <- read.table(routine, header = T, sep = "\t")
  TTN_pai2g_sim <- TTN_pai2g_sim $ Pai2g
  
  # summary(TTN_pai2g_sim)
  
  ttn_pai2g_exp <- 1.977227008827069E-4
  
  Si.simTemp <- TTN_pai2g_sim - ttn_pai2g_exp
  
  Si.sim <- c(Si.sim, Si.simTemp)
  
  n.sim <- length(Si.sim)
  
  Score.sim <- ( sum(Si.sim))^2 / (n.sim * var(Si.sim))
  # print(Score.sim)
  
  p.value <- (1 - pchisq(Score.sim, df=1)) / 2
  
  P.values <- c(P.values, p.value)
  
}

P.values

plot(P.values)

density(retC)
plot(density(retC), main = 'Plot 227 groups P-Values')


#######################################################
## return variances
## simulated TTN Pai2g through Rao's Score Test
VarianceBeta0 <- function(circle){
  
  ## 50k groups
  ## routine <- paste("D:/PhD/PhD/beta0/simulated_n2n1_", circle, ".txt", sep = "")
  
  ## 10k groups
  routine <- paste("D:/PhD/PhD/100kbeta0/simulated_n2n1_1.txt", sep = "")
  
  TTN_pai2g_sim <- read.table(routine, header = T, sep = "\t")
  TTN_pai2g_sim <- TTN_pai2g_sim $ Pai2g
  plot(TTN_pai2g_sim)
  # summary(TTN_pai2g_sim)
  
  ttn_pai2g_exp <- 1.977227008827069E-4
  
  Si.sim <- TTN_pai2g_sim - ttn_pai2g_exp
  
  Si.var <- var(Si.sim)
  
  return( Si.var ) 
}
########################

## calculate P-Values for 226 groups of simulated genotypes with 50K individuals in each group.
## initial PValues.rao as a null vector
var.rao.b0 <- NULL

## read in 226 * 50,000 Pai2|g, apply to Rao's Score Test formula, calculate the P-values

for(i in 0:1199){
  
  var.rao.b0 <- c(var.rao.b0, VarianceBeta0(i))
}

mean(var.rao.b0)

length(var.rao.b0)

