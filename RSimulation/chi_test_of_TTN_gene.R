## 06-14-2016
## Test for Loss of Homozygosity Simulation
##

## read all genes information from ALS dataset 
ALS_data <- read.table("D:/PhD/LizDeidentified_151002/LizDeidentified_151002/gene_samp_matrix_high_LOF_hom.txt", header = TRUE, sep = "\t")

## get TTN gene information
ttn <- ALS_data$TTN

summary(ttn)

## the \Pai_2|g of ttn gene is 3.6328594930717866E-5
## got it from D0606_TTN_CSV_fit_CCDS15Frame.java code
## this is the expected probability of homozygity rate for ttn gene
ttn_pai2g <- 3.6328594930717866E-5


## test H0: \beta = 1 using a score test:
## the ith person's contribution to the marginal score
Si <- ttn - ttn_pai2g
summary(Si)

## get the Si_mean
Si_mean = mean(Si)
Si_mean


Si_var = var(Si)
Si_var

