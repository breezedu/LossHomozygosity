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

## the length of n
n <- length(Si)

## simple score test: sct
st_simple <- ( sum(Si) )^2 / (var(Si) * n) 

## check sct
st_simple

var(Si)

##[1] 25.96067

1- pchisq(st_simple, df=1)
##[1] 3.485652e-07

( 1 - pchisq(25.96, df = 1)) / 2
##[1] 1.742826e-07





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
