//fibonacci series
//compute the nth item, 1000 > n > 2
class FiboCalc{ 
  
  static func calcFib(n: Int) -> Int{
      var nth = n - 1;
      var i=0, j=1, temp: Int;
      
      if(nth > 1000) {
        print("No way");
          return -1;
      }

      repeat {
        temp = j;
        j = j+i;
        i = temp;
        nth = nth - 1;    
      } while(nth > 0);
      
      return j; 
  }
}

print(FiboCalc.calcFib(n: 3));
print(FiboCalc.calcFib(n: 92));

//the below terminates with signal 4    
//print(FiboCalc.calcFib(n: 93));

//program to sum 2 nos, if the values are equal, triple their sum.
func summit(a: Int, b: Int) -> Int{
  if(a==b){
      return 3*(a+b);
  } else {
      return a+b;
  } 
}

print(summit(a: 10, b: 20));
