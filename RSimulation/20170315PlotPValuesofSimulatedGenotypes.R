

#################################################
## simulated TTN Pai2g
CalScore <- function(circle){
  
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

for(i in 0:300){
  retC <- c(retC, CalScore(i))
}

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

