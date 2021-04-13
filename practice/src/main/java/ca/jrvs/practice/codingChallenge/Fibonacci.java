package ca.jrvs.practice.codingChallenge;

public class Fibonacci {


  public int fibRecursive(int n) {
    if(n < 2)
      return n;
    return fibRecursive(n-1) + fibRecursive(n-2);

  }

  public int fibDynamic(int n){
    if (n==0)
      return 0;
    int dp[] = new int[n+1];

    //base cases
    dp[0] = 0;
    dp[1] = 1;

    for(int i = 2; i <=n; i++){
      dp[i] = dp[i-1] + dp[i-2];
    }
    return dp[n];
  }
}
