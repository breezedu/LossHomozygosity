##################################################################
## 
## 02-01-2017
## Test for Loss of Homozygosity Simulation
##
##################################################################
## read in Pai_2|g data from txt document:
## there are 3047 individuals

TTN_pai2g_observed <- read.table("D:/GitHub/LossHomozygosity/ALS_dataset/TTN_Pai2g_observed.txt", header = TRUE, sep = "\t")


## extract only $TTN row
TTN_pai2g_observed <- TTN_pai2g_observed$TTN
summary(TTN_pai2g_observed)


## plot the homozygous variants
plot(TTN_pai2g_observed)


## the \Pai_2|g of ttn gene is 3.6328594930717866E-5
## got it from D0606_TTN_CSV_fit_CCDS15Frame.java code
## this is the expected probability of homozygity rate for ttn gene
ttn_pai2g_expect <- 1.977227008827069E-4   ## when there are 414 LoF and 136 Missense variants;

ttn_pai2g_expect <- 3.6328594930727866E-5   ## when there are only 414 SNPs on the gene;

## test H0: \beta = 1 using a score test:
## the ith person's contribution to the marginal score
## Si = Pai_2|g - Sum( Pai_2|g'*Rho_g'), here the second part is ttn_pai2g_expect

Si <- TTN_pai2g_observed - ttn_pai2g_expect
summary(Si)


var(Si)

var(Si) * length(Si)

## simple score test: sct
st_simple <- ( sum(Si) )^2 / (var(Si) * n) 
( sum(Si))^2
## check sct
st_simple


#################################################
## simulated TTN Pai2g
CalScore <- function(circle){
  
  routine <- paste("D:/PhD/PhD/simulated_n2n1_", circle, ".txt", sep = "")
  TTN_pai2g_sim <- read.table(routine, header = T, sep = "\t")
  TTN_pai2g_sim <- TTN_pai2g_sim $ Pai2g
  
  # summary(TTN_pai2g_sim)
  
  ttn_pai2g_exp <- 3.63285E-5
  
  Si.sim <- TTN_pai2g_sim - ttn_pai2g_exp
  
  n.sim <- length(Si.sim)
  
  Score.sim <- ( sum(Si.sim))^2 / (n.sim * var(Si.sim))
  print(Score.sim)
  
  p.value <- (1 - pchisq(Score.sim, df=1)) / 2
  print( c(Score.sim, p.value) )
}


for(i in 0:9){
  CalScore(i)
}
  
CalScore(1)
  
  
  
TTN_pai2g_simulated <- read.table("D:/PhD/PhD/simulated_n2n1_0.txt", header=  TRUE, sep = "\t")

## simulated 
TTN_pai2g_simulated <- TTN_pai2g_simulated$Pai2g
summary(TTN_pai2g_simulated)


ttn_pai2g_expect <- 3.6328594930727866E-5   ## when there are only 414 SNPs on the gene;
Si.simulated <- TTN_pai2g_simulated - ttn_pai2g_expect 
summary(Si.simulated)
## the length of n = 10000
n.simulate <- length(Si.simulated)
n.simulate

##################################
## simulated 
st_simulated <- ( sum(Si.simulated))^2 / ( var(Si.simulated) * n.simulate)
st_simulated

##[1] 25.96067
(1 - pchisq(st_simulated, df = 1)) /2



##############################################################################
var(Si.simulated)

plot(TTN_pai2g_simulated)

1- pchisq(st_simple, df=1)
##[1] 3.485652e-07


( 1 - pchisq(st_simple, df = 1)) / 2
##[1] 1.742826e-07


##################################
## simulated 
( 1 - pchisq(1.0082, df = 1)) /2


#######################################################################
## after applied fisher information 
## after applied Monte Carol integration
#######################################################################


## alternatively
ttn_pai2g_sqr_montecarol <- 1.166E-4


Si_square2 <-  (TTN_pai2g_observed - ttn_pai2g_expect)^2 


sum(Si_square2)

I_beta <- ttn_pai2g_sqr_montecarol - ttn_pai2g_expect^2
I_beta

Sittn_pai2g_expect


## score test with fisher information
sc_fisher <- (sum(Si_square2) / I_beta )/n

sc_fisher
# 24.26783


1 - pchisq(sc_fisher, df = 1)
## 8.3827e-7

(1 - pchisq(24.26783, 1))/2
## 4.1913e-07
############################



###############################################
## 
## (sum(Si))^2 / (sum( I(beta) )

Score <- sum(Si)^2
Score

I_beta <- TTN_pai2g_observed^2 - ttn_pai2g_expect^2

sum(I_beta)

var(Si)

Score / sum(I_beta) 
# [1] 25.73214

(1 - pchisq(25.73214, 1)) / 2


sum(Si)^2 / (var(Si) * length(Si) )
# [1] 25.96067
(1 - pchisq(25.96067, 1)) / 2
