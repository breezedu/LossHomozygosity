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
TTN_pai2g_observed <- read.table("D:/GitHubRepositories//LossHomozygosity/ALS_dataset/TTN_Pai2g_observed.txt", header = TRUE, sep = "\t")

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
Cumulative = 0.08



sum(Si)/sqrt(length(Si)*var(Si))
## [1] 5.263353


1-pnorm(5.263353)
##[1] 7.072581e-08

help(pnorm)
mean(Si)


##[1] 0.005050642

mean(Si)*sqrt(length(Si))/sqrt(var(Si))
##[1] 5.263353

(mean(Si)*sqrt(length(Si))/sqrt(var(Si)))^2
##[1] 27.70288

(mean(Si)*sqrt(length(Si))/sqrt(var(Si)))^2/length(Si)
##[1] 0.009091855

Si_mean * sqrt(length(Si))/sqrt(var(Si))


Si_mean = mean(Si)
Si_mean * sqrt(length(Si))/sqrt(var(Si))
##[1] 5.263353

1- pchisq(27.7, 1)
##[1] 1.416627e-07


( 1 - pchisq(27.702, 1)) / 2
##[1] 7.075816e-08

##############################################
## after applied fisher information 
## after applied Monte Carol integration
ttn_pai2g_times_rho <- 3.6328594930727866E-5
ttn_pai2g_sqr_montecarol <- 1.8455E-5

Si_square2 <-  (TTN_pai2g_observed - ttn_pai2g_expect)^2 
Si_square2
mean(Si_square2)

var <- ttn_pai2g_sqr_montecarol - ttn_pai2g_expect^2
var

(mean(Si_square2) * sqrt(length(Si_square2))) /(ttn_pai2g_sqr_montecarol - ttn_pai2g_expect^2) 
#8466

chi_sqr2 <- Si_square2/(ttn_pai2g_sqr_montecarol - ttn_pai2g_times_rho^2) 
chi_sqr2

1 - pchisq(8466, 1)
