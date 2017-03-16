#################################################
##
## simulated TTN Pai2g through Rao's Score Test
RaosScoreBeta0 <- function(circle){
  
  ## 50k groups
  ## routine <- paste("D:/PhD/PhD/beta0/simulated_n2n1_", circle, ".txt", sep = "")
  
  ## 10k groups
  routine <- paste("D:/PhD/PhD/10kbeta0/simulated_n2n1_", circle, ".txt", sep = "")
  
  TTN_pai2g_sim <- read.table(routine, header = T, sep = "\t")
  TTN_pai2g_sim <- TTN_pai2g_sim $ Pai2g
  
  # summary(TTN_pai2g_sim)
  
  ttn_pai2g_exp <- 1.977227008827069E-4
  
  Si.sim <- TTN_pai2g_sim - ttn_pai2g_exp
  
  n.sim <- length(Si.sim)
  
  ## Score.sim = ( sum(Si.sim))^2 / ( n.sim * var(Si.sim) )
  I_beta <- TTN_pai2g_sim^2 - ttn_pai2g_exp^2
  
  Score.sim <- ( sum(Si.sim))^2 / (sum(I_beta) )
  
  ## Calculate p-values 
  p.value <- (1 - pchisq(Score.sim, df=1)) 
  
  print( c('Score: ', Score.sim, ' P-value: ', p.value) )
  return( p.value ) 
}
########################

## calculate P-Values for 226 groups of simulated genotypes with 50K individuals in each group.
## initial PValues.rao as a null vector
PValues.rao.b0 <- NULL

## read in 226 * 50,000 Pai2|g, apply to Rao's Score Test formula, calculate the P-values

for(i in 0:299){
  
  PValues.rao.b0 <- c(PValues.rao.b0, RaosScoreBeta0(i))
}

## density of p-values from Rao's Score Test
density(PValues.rao) 

## plot P-values from Rao's Score Test
plot(density(PValues.rao.b0), main = 'Plot 2000 Samples P-Values when beta=0')

hist(PValues.rao.b0, 
     breaks = 20,
     main = 'Histogram of PValues under Beta=0', 
     xlim = c(0,1),
     col = "blue",
     xlab = "P-vlues, simple size = 300",
     ylab = "Density")

mean( PValues.rao.b0 < 0.05)

mean( PValues.rao.b0 < 0.025)

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





PValues.rao <- NULL
for(i in 0:998){
  
  PValues.rao <- c(PValues.rao, RaosScore(i))
}

hist(PValues.rao,
     xlim = c(0,1))

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
