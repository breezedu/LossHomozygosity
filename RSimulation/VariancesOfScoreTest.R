

#######################################################
## return variances of each simulated dataset
##
## simulated TTN Pai2g through Rao's Score Test
## 
VarianceBeta0 <- function(circle){
  
  ## Read in dataset 100k groups
  routine <- paste("D:/PhD/PhD/10kbeta0/simulated_n2n1_", circle, ".txt", sep = "")
  
  TTN_pai2g_sim <- read.table(routine, header = T, sep = "\t")
  TTN_pai2g_sim <- TTN_pai2g_sim $ Pai2g
  
  ## summary(TTN_pai2g_sim)
  ## TTN Pai2|g expected
  ttn_pai2g_exp <- 1.977227008827069E-4
  
  ## the Score Vector
  Si.sim <- TTN_pai2g_sim - ttn_pai2g_exp
  
  ## the variance of the Score from current dataset
  Si.var <- var(Si.sim)
  
  print(var(Si.sim))
  
  return( Si.var ) 
}
########################

## calculate variances for 1200 groups of simulated genotypes with 100K individuals in each group.
## initial var.rao.b0 as a null vector
var.rao.b0 <- NULL

## read in 1200 *100,000 Pai2|g, apply to Rao's Score Test formula, calculate the variances
for(i in 0:1199){
  
  var.rao.b0 <- c(var.rao.b0, VarianceBeta0(i))
}

## now we have 1200 variances, calculate the mean
mean(var.rao.b0)


#####################################################
## Variance of Score across all simulated datasets
## Return Scores of each dataset 
AllScoreBeta0 <- function(circle){
  
  ## 100k groups
  routine <- paste("D:/PhD/PhD/10kbeta0/simulated_n2n1_", circle, ".txt", sep = "")
  
  TTN_pai2g_sim <- read.table(routine, header = T, sep = "\t")
  TTN_pai2g_sim <- TTN_pai2g_sim $ Pai2g
  
  ## summary(TTN_pai2g_sim)
  ## TTN Pai2|g expected
  ttn_pai2g_exp <- 1.977227008827069E-4
  
  ## the Score Vector
  Si.sim <- TTN_pai2g_sim - ttn_pai2g_exp
  
  ## return the Score  
  
  return( Si.sim ) 
}

##
Score.simulated <- NULL
for(i in 0:1199){
  
  Score.simulated <- c(Score.simulated, AllScoreBeta0(i))
  
}

## now we have 1200*100,000 Scores, calculate the variance
var(Score.simulated)

length(Score.simulated)


