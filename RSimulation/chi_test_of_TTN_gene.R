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

## read in Pai_2|g data from txt document:
TTN_pai2g_observed <- read.table("D:/PhD/TTN_Pai2g_observed.txt", header = TRUE, sep = "\t")

## extract only $TTN row
TTN_pai2g_observed <- TTN_pai2g_observed$TTN

## plot the homozygous variants
plot(TTN_pai2g_observed)


## the \Pai_2|g of ttn gene is 3.6328594930717866E-5
## got it from D0606_TTN_CSV_fit_CCDS15Frame.java code
## this is the expected probability of homozygity rate for ttn gene
ttn_pai2g_expect <- 3.6328594930717866E-5


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

## calculate the cumulative probability P(X^2 <= CV) = 0.24
## http://stattrek.com/online-calculator/chi-square.aspx
Cumulative = 0.08

