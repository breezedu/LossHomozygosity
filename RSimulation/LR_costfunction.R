


x <- c(3, 1, 0, 4)
y <- c(2, 2, 1, 2)

m <- length(x)

x
y
m 

x <- c(1, 2, 2, 3, 3, 4, 5, 6, 6, 6, 8, 10)
y <- c(-890, -1411, -1560, -2220, -2091, -2878, -3537, -3268, -3920, -4163, -5471, -5157)

## cost function:
cost <- function(theta0, theta1, x, y){
  
  m <- length(x) 
  J <- 0
  
  for( i in 1:m){
    
    J = J + (theta0 + theta1*x[i] -y[i])^2
    
  } # end for()
  
  J = J/(2 * m)
  
  return(J)
  
} ## end cost function;


cost(0, 1, x, y)

cost(-569.6, -530.9, x, y)

cost(-569.6, 530.9, x, y)

cost( -1780, -530.9, x, y)

cost( -1780.0, 530.9, x, y)


## initial theta0 theta1
theta0 <- 0
theta1 <- 1

alpha <- mean(x)/100

## repeat untill converage
GradientDescent <- function( theta0, theta1, alpha, x, y){
  
  
  temp0 <- theta0 - alpha * (1/length(x)) * sum(theta0  + theta1 * x -y)
  
  temp1 <- theta1 - alpha * (1/length(x)) * sum( ( theta0 + theta1*x - y) * x)
  
  print( c('recurssion', temp0, temp1) )
  
  if(abs(temp0 - theta0) < 0.00001 & abs(temp1 - theta1) < 0.00001 ){
    
    print( c(theta0, theta1) )
    
    return( c(theta0, theta1))
    
  } else {
    
    theta0 <- temp0
    theta1 <- temp1
    
    #alpha <- alpha * 0.1
    
    GradientDescent( theta0, theta1, alpha, x, y)
    
  } ## end if-else conditions

} ## end GradientDescent() function; 

## Run GradientDescent() with paramenters and data
coeff <- GradientDescent(theta0, theta1, alpha, x, y)

coeff

plot(x, y)

lines( x, coeff[1] + coeff[2] * x, col = "red", lwd = 2)


## try lme4 package
library(lme4) 

data.1 <- cbind(x, y)
colnames(data.1) <- c("xlab", "ylab")


data.1 <- data.frame(data.1)
lm(ylab ~ xlab, data = data.1)
