###########################################################################################################
##
## Homozygosity Project
## Date 12-29-2015
## Aim: Simulation Loss of Homozygosity Population Strat 1
## @ authors: Andrew Allen 
## @ student: Jeff Du
## Data source: Simulation
## Models:  Poisson
## Parameters: 
##
## 
###########################################################################################################


nsites <- 5         # number of polymorphic qualifying sites in gene
np1 <- 1000         # sample size popn1
np2 <- 1000         # sample size popn2
n <- np1+np2
p.upper <- 0.05     # upper bound on qualifying variants
b.v <- .95          # baseline viability (probability of being viable give zero or one affected gene copies)
bta <- 1            # relative risk of viability given 2 affected copies versus baseline
nsim <- 1           # 
s <- c(rep(0,nsim)) # 


g <- array(0,dim=c(n,2*nsites,2))              # two matric, dim: 2000, 10, 2
x <- c(rep(0,n))                               # x replicates n=2000 values in 0
v <- c(rep(0,n))                               # v ...
n1 <- c(rep(0,n))                              # n1 ...
n2 <- c(rep(0,n))                              # n2 ...
pi2.g <- c(rep(0,n))                           # pi2.g ...


p1 <- runif(nsites, min=2/(2*np1), max=p.upper)   # generate random deviates from 0.001 to 0.5
p2 <- runif(nsites, min=2/(2*np2), max=p.upper)   # generate random deviates from 0.001 to 0.5
p <- c(p1*np1/n, p2*np2/n)                        # merge p1*2 and p2*2



## prob, a matrix with 3 rows and 10 cols
## row[0] ~ n of X = 0 no 
## row[1] ~ n of X = 1 het
## row[2] ~ n of X = 2 hom
## each rol represents a gene
prob <- matrix(0,3,2*nsites)      # site specific genotype probabilities

## generate and assign values to prob[][]
for(i in 1:(2*nsites)){
	prob[1,i] <- (1-p[i])^2
	prob[2,i] <- 2*p[i]*(1-p[i])
	prob[3,i] <- p[i]^2
}


## the prob matrix:
#> prob
###           [,1]         [,2]         [,3]         [,4]         [,5]         [,6]         [,7]         [,8]         [,9]        [,10]
#[1,] 9.828186e-01 0.9675650920 9.948101e-01 9.945325e-01 0.9691299458 9.938453e-01 0.9679320811 0.9750880297 0.9712017451 9.849940e-01
#[2,] 1.710694e-02 0.0321675484 5.183147e-03 5.460042e-03 0.0306280643 6.145237e-03 0.0318066244 0.0247548556 0.0285878797 1.494928e-02
#[3,] 7.444081e-05 0.0002673596 6.751292e-06 7.493987e-06 0.0002419898 9.499450e-06 0.0002612945 0.0001571148 0.0002103752 5.672141e-05


#########################################################################
##
## Figure out how many genotype you can have given certain number of loci
##
## function_baseB: 
baseB <- function(x, digits, B){
	baseB<-c(rep(0,digits))
	temp<-x
	count<-1
	while(temp>0){
		baseB[count] <- temp%%B
		temp <- temp%/%B
		count < -count+1
	}
	return(baseB)	
}
###################################################################


###################################################################
## function_bigN
bigN<-function(digits){
	bigN<-0
	for(i in 1:digits){
		bigN<-bigN+2*3^(i-1)
	}
	return(bigN)
}
###################################################################

## mu denotes mutations?
mu <- 0
for(i in 1:bigN(2*nsites)){
	g.temp <- baseB(i,2*nsites,3)
	n1.temp <- sum(g.temp==1)
	n2.temp <- sum(g.temp==2)
	if(n1.temp<=1 & n2.temp==0) {pi2.g.temp<-0}
	if(n1.temp>1 & n2.temp==0) {pi2.g.temp<-1-(1/2)^(n1.temp-1)}
	if(n2.temp>0) {pi2.g.temp<-1}
	
	rho <- 1
	for(j in 1:2*nsites){
		rho<-rho*prob[g.temp[j]+1,j]
	}
	mu<-mu+rho*pi2.g.temp
}

## after for loop, mu = 27702.14


## nnz 
nnz <- 0


########################################
## 
for(j in 1:nsim){
	print(j)
  
  
  #simulate data under alternative
  count <- 1
  
  ### while loop for population 1
  while(count<=np1){

    for(c in 1:2){
      for(l in 1:nsites){
        g[count,l,c]<-rbinom(1,1,p1[l])         ## genotypes for population.
      }
    }

    ## count -> person
    ## ,, site
    ## 1 or 2, gene copy
    temp1<-sum(g[count,,1])            ## number of sites person 'count' has a affected 1st gene copy.
    temp2<-sum(g[count,,2])            ## number of sites person 'count' has a affected 2nd gene copy.

    x[count] <- (temp1>0)+(temp2>0)    ## number of gene copies that are affected for individual 'count';
    if(x[count]<2){p.v<-b.v}           ## baseline of virable
    else{p.v<-b.v*bta}                 ## p.v => prob of being viable 
	
    v[count]<-rbinom(1,1,p.v)          ## viability of person 'count'
    if(v[count]==1){count<-count+1}    

  } #end while(count<=np1)             ## the while loop for population 1

  
  
  ### while loop for population 2 
  while(count<=n){

    for(c in 1:2){
      for(l in (nsites+1):(2*nsites)){
        g[count,l,c]<-rbinom(1,1,p2[l-nsites])
      }
    }

    temp1<-sum(g[count,,1])
    temp2<-sum(g[count,,2])

    x[count]<-(temp1>0)+(temp2>0)
    if(x[count]<2){p.v<-b.v}
    else{p.v<-b.v*bta}
    v[count]<-rbinom(1,1,p.v)
    if(v[count]==1){count<-count+1}

  } #end while(count<=n)               ## the while loop for population 2

  
  ## g.obs: genotypes observed, a 1000*10 matrix
  ## so, next step to do, is to plug data from matric in LizDeidentified.zip?
  g.obs<-g[,,1]+g[,,2]

  
  ###############################
  ## for loop for Pie_2|g
  for(i in 1:n){
    n1[i] <- sum(g.obs[i,]==1)
    n2[i] <- sum(g.obs[i,]==2)
    if(n1[i]<=1 & n2[i]==0) {pi2.g[i]<-0}
    if(n1[i]>1 & n2[i]==0) {pi2.g[i]<-1-(1/2)^(n1[i]-1)}
    if(n2[i]>0) {pi2.g[i]<-1}
    
  } #end for i in 1:n loop;

  
  ####################################################
  ## s.1,.....,s.nsim <= statistic in nsim simulations
  s.j<-pi2.g-mu
  if(var(s.j)!=0){
    s[j]<-sum(s.j)/sqrt(n*var(s.j))
    nnz<-nnz+1
  }

}     ## end for j in 1:nism loop
########################################
print(nnz)


########################################
## POWER!
# sum 
sum(s<qnorm(.05))/nsim

##? s<qnorm(0.05)) = 1, nsim = 1. Confused...

## END