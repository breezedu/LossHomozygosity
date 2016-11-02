## 06-14-2016
## Test for Loss of Homozygosity Simulation
##

## read all genes information from ALS dataset 
ALS_hom <- read.table("D:/PhD/LizDeidentified_151002/LizDeidentified_151002/gene_samp_matrix_high_LOF_het.txt", header = TRUE, sep = "\t")

ALS_het <- read.table("D:/PhD/LizDeidentified_151002//LizDeidentified_151002/gene_samp_matrix_high_LOF_hom.txt", header = TRUE, sep = "\t")

## get TTN gene information
## ttn_hom is the count of n2
## ttn_het is the count of n1
ttn_hom <- ALS_hom$TTN
ttn_het <- ALS_het$TTN

## for a given individual, 
#### the \Pi_2|g would be 0, if both n1 <=1 & n2=2
#### the \Pi_2|g would be 1 - (0.5)^(n1-1), if n1 >1 & n2=0
#### the \Pi_2|g would be 1, if n2 > 0

summary(ttn_hom)
summary(ttn_het)



##################################################################
## read in Pai_2|g data from txt document:
TTN_pai2g_observed <- read.table("D:/GitHub/LossHomozygosity/ALS_dataset/TTN_Pai2g_observed.txt", header = TRUE, sep = "\t")

## extract only $TTN row
TTN_pai2g_observed <- TTN_pai2g_observed$TTN

summary(TTN_pai2g_observed)

## plot the homozygous variants
plot(TTN_pai2g_observed)


## the \Pai_2|g of ttn gene is 3.6328594930717866E-5
## got it from D0606_TTN_CSV_fit_CCDS15Frame.java code
## this is the expected probability of homozygity rate for ttn gene
ttn_pai2g_expect <- 1.977227008827069E-4


## test H0: \beta = 1 using a score test:
## the ith person's contribution to the marginal score
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

Chi_critical <- Si_mean^2 * length(Si) / sum(Si_squre) 
Chi_critical

Si_mean * sqrt(length(Si))/sqrt(var(Si))

## calculate the cumulative probability P(X^2 <= CV) = 0.24
## http://stattrek.com/online-calculator/chi-square.aspx
## Cumulative = 0.08



sum(Si)/sqrt(length(Si)*var(Si))
## [1] 5.095161


1-pnorm(5.095161)
##[1] 1.742225e-07

help(pnorm)
mean(Si)
##[1] 0.004889248


mean(Si)*sqrt(length(Si))/sqrt(var(Si))
##[1] 5.095161

(mean(Si)*sqrt(length(Si))/sqrt(var(Si)))^2
##[1] 25.96067

(mean(Si)*sqrt(length(Si))/sqrt(var(Si)))^2/length(Si)
##[1] 0.008520075

Si_mean * sqrt(length(Si))/sqrt(var(Si))


Si_mean = mean(Si)
Si_mean * sqrt(length(Si))/sqrt(var(Si))
##[1] 5.095161

1- pchisq(25.96067, 1)
##[1] 3.484442e-07


( 1 - pchisq(25.96067, 1)) / 2
##[1] 1.742221e-08



##############################################
## after applied fisher information 
## after applied Monte Carol integration
ttn_pai2g_times_rho <- 1.977227008827069E-4
ttn_pai2g_sqr_montecarol <- 1.8455E-4

Si_square2 <-  (TTN_pai2g_observed - ttn_pai2g_expect)^2 
Si_square2
mean(Si_square2)

var_fisher <- ttn_pai2g_sqr_montecarol - ttn_pai2g_expect^2
var_fisher

## is this equation correct??
(mean(Si_square2) * sqrt(length(Si_square2))) / sqrt(var_fisher)

## 
(mean(Si_square2) * sqrt(length(Si_square2))) /sqrt(ttn_pai2g_sqr_montecarol - ttn_pai2g_expect^2) 
#36.385

chi_sqr2 <- sum(Si_square2)/(ttn_pai2g_sqr_montecarol - ttn_pai2g_times_rho^2)/length(Si_square2) 
chi_sqr2 

(1 - pchisq(15.3307, 1))/2
## 1.619448e-09
############################
