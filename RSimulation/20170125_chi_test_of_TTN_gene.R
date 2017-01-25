
## 10-14-2016
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
## plot(TTN_pai2g_observed)

## the \Pai_2|g of ttn gene is 3.6328594930717866E-5
## got it from D0606_TTN_CSV_fit_CCDS15Frame.java code
## this is the expected probability of homozygity rate for ttn gene
ttn_pai2g_expect <- 1.977227008827069E-4

## test H0: \beta = 1 using a score test:
## the ith person's contribution to the marginal score
## Si = Pai_2|g - Sum( Pai_2|g'*Rho_g'), here the second part is ttn_pai2g_expect

Si <- TTN_pai2g_observed - ttn_pai2g_expect
summary(Si)

## get the Si_mean
Si_mean = mean(Si)
Si_mean
Si_var = var(Si)
Si_var

## (Si)^2 / [(1/n)*Sum(Si)] ~ (X^2)_1
## P-value P( Si_mean / (Sum(Si)/n) <= X^2_1 ) == 0.05
##
Si_squre <- Si^2

## Chi_critical <- Si_mean^2 * length(Si) / sum(Si_squre) 
## Chi_critical

Si_mean * sqrt(length(Si))/sqrt(var(Si))

############################################ 
## Sum( Si ) / [Sqrt(n) * Var(Si)]
##
sum(Si)/sqrt(length(Si)*var(Si))
## ##
mean(Si)*sqrt(length(Si))/sqrt(var(Si))
## [1] 5.095161
## [1] 5.095161

1-pnorm(5.095161)
##[1] 7.072581e-08 /or/ 1.742225e-07

## ###########################################
## Sum( Si ) / (Var(Si) * n)
## 
mean(Si)*sqrt(length(Si))/sqrt(var(Si))
##This follows normal distribution;

(mean(Si)*sqrt(length(Si))/sqrt(var(Si)))^2
## this follows chi-square distribution with freedom degree 1
##[1] 25.96067
## or 
sum(Si)^2 / Si_var / length(Si)
##[1] 25.96067

1- pchisq(25.96, 1)
##[1] 3.485652e-07

( 1 - pchisq(25.96, 1)) / 2
##[1] 1.742826e-07




##############################################
## after applied fisher information 
## after applied Monte Carol integration
##############################################

ttn_pai2g_times_rho <- 1.977227008827069E-4



## alternatively
ttn_pai2g_sqr_montecarol <- 1.166E-4

Si_square2 <-  (TTN_pai2g_observed - ttn_pai2g_expect)^2 
mean(Si_square2)
sum(Si_square2)

var_fisher <- ttn_pai2g_sqr_montecarol - ttn_pai2g_expect^2
var_fisher
ttn_pai2g_expect


## is this equation correct??
sum(Si_square2) / (var_fisher * length(Si_square2))
chi_sqr2 <- sum(Si_square2)/(ttn_pai2g_sqr_montecarol - ttn_pai2g_times_rho^2) /length(Si_square2)
chi_sqr2 
## [1] 23.52902

1 - pchisq(23.529, 1)
## 1.230451e-06

(1 - pchisq(23.529, 1))/2
## 6.152254e-07
############################

## this is the Pai_2g square, simulated by monte carol, 
## everytime the value would be slight different after simulation
ttn_pai2g_sqr_montecarol <- 3.824E-8

