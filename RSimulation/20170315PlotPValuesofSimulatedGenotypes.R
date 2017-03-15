

#################################################
## simulated TTN Pai2g
CalScore <- function(circle){
  
  ## 50k groups
  ## routine <- paste("D:/PhD/PhD/50kSampleSize/simulated_n2n1_", circle, ".txt", sep = "")
  
  ## 10k groups
  routine <- paste("D:/PhD/PhD/simulated_n2n1_", circle, ".txt", sep = "")
  
  TTN_pai2g_sim <- read.table(routine, header = T, sep = "\t")
  TTN_pai2g_sim <- TTN_pai2g_sim $ Pai2g
  
  # summary(TTN_pai2g_sim)
  
  ttn_pai2g_exp <- 1.977227008827069E-4
  
  Si.sim <- TTN_pai2g_sim - ttn_pai2g_exp
  
  n.sim <- length(Si.sim)
  
  Score.sim <- ( sum(Si.sim))^2 / (n.sim * var(Si.sim))
  # print(Score.sim)
  
  p.value <- (1 - pchisq(Score.sim, df=1)) / 2
  
  print( c('Score: ', Score.sim, ' P-value: ', p.value) )
  
  return( p.value ) 
}

retC <- NULL

for(i in 0:50){
  retC <- c(retC, CalScore(i))
}
density(retC)
plot(density(retC), main = 'Plot 51 groups P-Values')

retC <- NULL

for(i in 125:207){
  
  
  retC <- c(retC, CalScore(i))
}

density(retC)
plot(density(retC), main = 'Plot 227 groups P-Values')


for(i in 301:700){
  retC <- c(retC, CalScore(i))
}

for( i in 701:999){
  retC <- c(retC, CalScore(i))
}

density(retC)

PValues <- retC 

plot(density(PValues),
     main = "Plot P-values from simulated genotypes")



###################################################################


#################################################
## simulated TTN Pai2g through Rao's Score Test
RaosScore <- function(circle){
  
  ## 50k groups
  routine <- paste("D:/PhD/PhD/50kSampleSize/simulated_n2n1_", circle, ".txt", sep = "")
  
  ## 10k groups
  ## routine <- paste("D:/PhD/PhD/simulated_n2n1_", circle, ".txt", sep = "")
  
  TTN_pai2g_sim <- read.table(routine, header = T, sep = "\t")
  TTN_pai2g_sim <- TTN_pai2g_sim $ Pai2g
  
  # summary(TTN_pai2g_sim)
  
  ttn_pai2g_exp <- 1.977227008827069E-4
  
  Si.sim <- TTN_pai2g_sim - ttn_pai2g_exp
  
  n.sim <- length(Si.sim)
  
  I_beta <- TTN_pai2g_sim^2 - ttn_pai2g_exp^2
  
  
  # Score / sum(I_beta) 
  # [1] 25.73214
  
  Score.sim <- ( sum(Si.sim))^2 / (sum(I_beta) )
  # print(Score.sim)
  
  p.value <- (1 - pchisq(Score.sim, df=1)) / 2
  
  print( c('Score: ', Score.sim, ' P-value: ', p.value) )
  
  return( p.value ) 
}
########################

retC <- NULL

for(i in 125:200){
  retC <- c(retC, RaosScore(i))
}

density(retC)
plot(density(retC), main = 'Plot 51 groups P-Values')

retC <- NULL

for(i in 0:225){
  
  
  retC <- c(retC, RaosScore(i))
}

density(retC)
plot(density(retC), main = 'Plot 227 groups P-Values')




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
