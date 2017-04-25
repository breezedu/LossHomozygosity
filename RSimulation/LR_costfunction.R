


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

library(lme4) 



data.1 <- cbind(x, y)
colnames(data.1) <- c("xlab", "ylab")


data.1 <- data.frame(data.1)
lm(ylab ~ xlab, data = data.1)
