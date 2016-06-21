##
##
## Homozygosity Project
## Date 12-29-2015
## Aim: Simulation Loss of Homozygosity Genes
## @ authors: Andrew Allen 
## @ student: Jeff Du
## Data source: 
## Models:  Poisson
## Parameters: 


##########################################
## Part One Read in the data
## create the tables
##########################################



## initialize the parameters: 
## sites/size/upper bound/baeline/
nsites<-10              #number of polymorphic qualifying sites in gene
n<-5000                 #sample size
p.upper<-0.01           #upper bound on qualifying variants
b.v<-.95                # baseline viability (probability of being viable give zero or one affected gene copies)
bta<-1/10               # relative risk of viability given 2 affected copies versus baseline
nsim<-100               #
s<-c(rep(0,nsim))       #


g<-array(0,dim=c(n,nsites,2))
x<-c(rep(0,n))
v<-c(rep(0,n))
n1<-c(rep(0,n))
n2<-c(rep(0,n))
pi2.g<-c(rep(0,n))
p<-runif(nsites,min=2/(2*n),max=p.upper)




prob<-matrix(0,3,nsites) # site specific genotype probabilities
for(i in 1:nsites){
	prob[1,i]<-(1-p[i])^2
	prob[2,i]<-2*p[i]*(1-p[i])
	prob[3,i]<-p[i]^2
}

baseB<-function(x,digits,B){
	baseB<-c(rep(0,digits))
	temp<-x
	count<-1
	while(temp>0){
		baseB[count]<-temp%%B
		temp<-temp%/%B
		count<-count+1
	}
	return(baseB)	
}

bigN<-function(digits){
	bigN<-0
	for(i in 1:digits){
		bigN<-bigN+2*3^(i-1)
	}
	return(bigN)
}


mu<-0
for(i in 1:bigN(nsites)){
	g.temp<-baseB(i,nsites,3)
	n1.temp<-sum(g.temp==1)
	n2.temp<-sum(g.temp==2)
	if(n1.temp<=1 & n2.temp==0){pi2.g.temp<-0}
	if(n1.temp>1 & n2.temp==0){pi2.g.temp<-1-(1/2)^(n1.temp-1)}
	if(n2.temp>0){pi2.g.temp<-1}
	rho<-1
	for(j in 1:nsites){
		rho<-rho*prob[g.temp[j]+1,j]
	}
	mu<-mu+rho*pi2.g.temp
}


nnz<-0


for(j in 1:nsim){
	print(j)
  
  
  #simulate data under alternative
  count<-1
  while(count<=n){

	  for(c in 1:2){
		  for(l in 1:nsites){
		    g[count,l,c]<-rbinom(1,1,p[l])
			  
		    } #end for(l in 1:nsites)
	  } #end for(c in 1:2)

	  temp1<-sum(g[count,,1])
	  temp2<-sum(g[count,,2])

	  x[count]<-(temp1>0)+(temp2>0)
	  if(x[count]<2){p.v<-b.v}
	  else{p.v<-b.v*bta}
	  v[count]<-rbinom(1,1,p.v)
	  if(v[count]==1){count<-count+1}

  } #end while

  g.obs<-g[,,1]+g[,,2]


  for(i in 1:n){
	  n1[i]<-sum(g.obs[i,]==1)
	  n2[i]<-sum(g.obs[i,]==2)
	  if(n1[i]<=1 & n2[i]==0){pi2.g[i]<-0}
	  if(n1[i]>1 & n2[i]==0){pi2.g[i]<-1-(1/2)^(n1[i]-1)}
	  if(n2[i]>0){pi2.g[i]<-1}
  } #end for(i in 1:n)


  s.j<-pi2.g-mu
  if(var(s.j)!=0){
    s[j]<-sum(s.j)/sqrt(n*var(s.j))
    nnz<-nnz+1
  } #end if(var(s.j)!=0)

} #end for(j in 1:nsim){

sum(s<qnorm(.05))/nsim

