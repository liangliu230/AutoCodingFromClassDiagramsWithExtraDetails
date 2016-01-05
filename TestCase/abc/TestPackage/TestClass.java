package TestPackage;
public final  class TestClass extends TestPackage.AbstractClass   implements TestPackage.Interfaces.IAddIntToList   
{
private final  java.util.List<Integer> _List ;


public   TestClass (  ) 
{
_List=new java.util.ArrayList<Integer>();
}

@Override
public  void AddIntToList ( int number ) 
{
_List.add(number);
}


public static final  void print ( String str ) throws UnsupportedOperationException 
{
if(str.length()>2){
System.out.println("larger than 2");}
else{
System.out.println("less or equal to 2");}
switch(str){
case "abc":
System.out.println("is abc");
break;
case "def":
System.out.println("is def");
break;
default:
System.out.println("other");
break;}
for(int index=0;index<str.length();index+=1){
System.out.println(str.charAt(index));
int wIndex;
wIndex = 0;
while(wIndex<str.length())
System.out.println(str.charAt(wIndex));
wIndex+=(1);}
for(char ch:str.toCharArray()){
System.out.println(ch);}
try{
throw new UnsupportedOperationException("not supported yet");}
catch(UnsupportedOperationException ex){
System.out.println("exception catched");}
}

}