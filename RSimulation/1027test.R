##
##
xVec <- 1:3



myF1 <- function(xVec){
  
  out <- NULL
  for(i in 1:length(xVec)){
    
    out <- c(out, xVec[i]^i)
    
  }
  
  return(list(out1 = out))
  
}

myF1(xVec)

myF2 <- function(xVec){
  
  out <- NULL
  
  for( i in 1:length(xVec)){
    
    out <- c(out, xVec[i]^i / i)
  }
  return(list(out2 = out) )
  
}

myF2(xVec)



myF3 <- function(x, n){
  
  if(length(x)!= 1) {
    
    out <- 'Input x should be a single integer!'
    
  } else if (    round(n, 0)!=n | n < 1 ) {
    
    out <- 'Input n should be a positive integer'
    
  } else {
    
    out <- 1
    for( i in 1:n){
      out <- out + x^i / i
    } 
  }
  
  return(list(sum1 <- out) ) }


x <- 1
n <- 3
myF3(x, n)


n <- 3
r <- 2

make.matrix <- function(n, r){
  
  matrix.A <- matrix(NA, n, n)

  for( i in 1:n){
  
    for( j in 1:n){
    
      matrix.A[i, j] <- r ^ abs(i-j)
    
    }
  
  }

  return(list(mat = matrix.A))
  
}

make.matrix(3, 2)
make.matrix(3, 1.5)
  

moving.avg <- function(xVec, maLen){
  
  len <- length(xVec)
  
  end <- len - maLen + 1
  
  maSeq <- NULL
  
  for( i in 1:end){
    
    subseq <- xVec[i: (i + maLen -1)]
    
    maSeq <- c(maSeq, mean(subseq))
    
    print(subseq)
  }
  
  return(list(maSeq=maSeq))
  
}  
  
moving.avg(1:10, 5)


