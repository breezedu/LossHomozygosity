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


geneName <- "TTN"
##################################################################
## read in Pai_2|g data from txt document:
doc_name <- "D:/GitHub/LossHomozygosity/ALS_dataset/" + geneName + "_Pai2g_observed.txt"

gene_pai2g_observed <- read.table("D:/GitHubRepositories/LossHomozygosity/ALS_dataset/Observed_Pai2g_allgenes/A1BG_Pai2g_observed_test.txt", header = TRUE, sep = "\t")

## extract only $TTN row
gene_pai2g_observed <- gene_pai2g_observed$geneName

## plot the homozygous variants
plot(gene_pai2g_observed)


## the \Pai_2|g of ttn gene is 3.6328594930717866E-5
## got it from D0606_TTN_CSV_fit_CCDS15Frame.java code
## this is the expected probability of homozygity rate for ttn gene
gene_pai2g_expect <- 2.0655066872829075E-7


## test H0: \beta = 1 using a score test:
## the ith person's contribution to the marginal score
Si <- gene_pai2g_observed - gene_pai2g_expect
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



########################################################

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

