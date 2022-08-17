package ci583.htable.impl;

//This code will execute and it should be noted that test insert will take around 40 seconds to complete it just takes time.
//^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

import java.math.BigInteger;
import java.util.Collection;
import java.util.*;
public class Hashtable<V> {

	private static Object[] arr; //an array of Pair objects, where each pair contains the key and value stored in the hashtable
	private  int max; //the size of arr. This should be a prime number
	private  int itemCount; //the number of items stored in arr
	private final double maxLoad = 0.6; //the maximum load factor

	public enum PROBE_TYPE {
		LINEAR_PROBE, QUADRATIC_PROBE, DOUBLE_HASH;
	}


	PROBE_TYPE probeType; //the type of probe to use when dealing with collisions
	private final BigInteger DBL_HASH_K = BigInteger.valueOf(8);

	/**
	 * Create a new Hashtable with a given initial capacity and using a given probe type
	 * @param initialCapacity
	 * @param pt
	 */

	public Hashtable(int initialCapacity, PROBE_TYPE pt) {
		probeType = pt; // setting the probe type to pt which should be a given type.
		// first step is we need to check if our max which is going to be our arr length is a prime number.
		// to do this i will be using the isPrime method to determine if max is prime by using a simple if statement first i have to build isPrime to be called.
		if(isPrime(initialCapacity) == true){//if initialCapacity is true its prime and then we will set max to the intial capacity however , if its not a prime number then it will be set to the next prime value.
		this.max = initialCapacity; //Max is a declared unilnizlased varible which we are setting to initialCapacity which is the size of array arr.
		}else if(isPrime(nextPrime(initialCapacity)) == true){//Using method nextPrime to to find the next smallest possbile prime number and then checking it is prime.
		this.max = nextPrime(initialCapacity);// if the initialCapacity is equal false before this will find its next prime number and increment the size to that value.
		}
		itemCount = 0;//No items have been added to the array yet so the item count will be 0.
		arr = new Object[max]; // This is creating a new array arr with a new size which should be a prime number.
	}

	/**
	 * Create a new Hashtable with a given initial capacity and using the default probe type
	 * @param initialCapacity
	 */
	public Hashtable(int initialCapacity) {
		probeType = PROBE_TYPE.LINEAR_PROBE;
		if(isPrime(initialCapacity) == true){//if initialCapacity is true then we will set max to the intial capacity however , if its not a prime number then it will be set to the next prime value.
			this.max = initialCapacity; //Max is a declared unilnizlased varible which we are setting to initialCapacity which is the size of array arr.
		}else if(isPrime(nextPrime(initialCapacity)) == true){
			this.max = nextPrime(initialCapacity);// if the initialCapacity is equal false before this will find its next one and increment the size to that value.
		}
		itemCount = 0;// The item will be set to 0 as we have just creates the array, meaning no items will be present.
		arr = new Object[max];//Using max to set t he length of the array.
	}

	/**
	 * Store the value against the given key. If the loadFactor exceeds maxLoad, call the resize
	 * method to resize the array. the If key already exists then its value should be overwritten.
	 * Create a new Pair item containing the key and value, then use the findEmpty method to find an unoccupied
	 * position in the array to store the pair. Call findEmpty with the hashed value of the key as the starting
	 * position for the search, stepNum of zero and the original key.
	 * containing
	 * @param key
	 * @param value
	 */
	public void put(String key, V value) {// best time is constant O(1) and worst time is O(n^2)
		if (getLoadFactor() > maxLoad) { // when the current load factor exceeds the max load factor then we will need to resize the array so that it will reduce the load factor for the array.
			resize(arr);//This will call method resize and adjust the array. This will perform at O(n^2) which is the worst performance.
		}
		if(key == null){
			throw new IllegalArgumentException("Key is null");//If the key is not present within the array then it throw an exception. This is the Best Performance at constant time.
		} else {
			int hashed = hash(key);//Hashes the given key to be used for the position its found.
			int start = findEmpty(hashed,0,key);// calls method find empty to look for an empty location to store a in key and value.
			if(hasKey(key) == true){//This checks if the key exists within the array already. if it does then it return true.
				((Pair) arr[hashed]).value =  value; //This overwrites the currently stored version of that key.
			}else{
				//If the key doesn't exist within the hashtable then you can store it as a new set.
				arr[start] = new Pair(key,value);
				// If the item is new it will increment the itemCount.
				itemCount++;
			}
		}
	}
	/**
	 * Get the value associated with key, or return null if key does not exists. Use the find method to search the
	 * array, starting at the hashed value of the key, stepNum of zero and the original key.
	 * @param key
	 * @return
	 */
	//need to fix
	public V get(String key) {//Performs at O(n)
		int start = hash(key);//Start is to be set to the hash index of where the key would be stored. it does this by hashing the key.
		return find(start,key,0);//This will call method find and use varible start as the start position to start searching. When this is called and run this will perform at linear time due to method find running at linear time.
	}
	/**
	 * Return true if the Hashtable contains this key, false otherwise
	 * @param key
	 * @return
	 */
	public boolean hasKey(String key) {//Performs at O(n)
		if(get(key) == null){//This will use the get key method to see if a key is present if the key isn't then its null. This statement will run at linear time due to the method call of get.
			return false;//If the key doesn't exit then its null ans should return false.
		}
		return true; // If the key has been found then it will return true , that is currently present within the list of keys.
	}
	/**
	 * Return all the keys in this Hashtable as a collection
	 * @return
	 */
	public Collection<String> getKeys() {//Performs at O(n)
		List<String> keys = new ArrayList();//Creating a new list to store the keys within, can be any collection type.
		int i;
		for(i = 0; i < arr.length; i++){//Loop used to find all the current keys to store them within the list.
			if(arr[i] ==null){ //If the array position is empty then continue to the next position.
				continue;
			}else{//However, if the the array position is not null then create a new of the key within array[i].
				Pair obj = (Pair) arr[i];
				keys.add(obj.key);//tells the obj to only add the key to the list. This performs at linear time.
			}
		}
		return keys;//Return the list which should have keys within it.
	}
	/**
	 * Return the load factor, which is the ratio of itemCount to max
	 * @return
	 */
	public double getLoadFactor() { //O(1) performance
		// To Calculate the load Factor its the amount of values divided by the total length.
		//return itemCount / arr.length itemCount which is the number of values we have / the array length total spaces we have.
		return ((double) itemCount) / max;//Returning the load factor however, itemCount is being casted as a double so that it can return a decimal else it will return an integer that will round itself up.
	}
	/* The load factor is the ratio of the amount of items stored divided by the entire length*/
	/**
	 * return the maximum capacity of the Hashtable
	 * @return
	 */
	public int getCapacity() { //O(1) performance
	return max;} // This is a straight forward return as the array will be set to max. Therefore the capacity of the table will be max.
	/**
	 * Find the value stored for this key, starting the search at position startPos in the array. If
	 * the item at position startPos is null, the Hashtable does not contain the value, so return null.
	 * If the key stored in the pair at position startPos matches the key we're looking for, return the associated
	 * value. If the key stored in the pair at position startPos does not match the key we're looking for, this
	 * is a hash collision so use the getNextLocation method with an incremented value of stepNum to find
	 * the next location to search (the way that this is calculated will differ depending on the probe type
	 * being used). Then use the value of the next location in a recursive call to find.
	 * @param startPos
	 * @param key
	 * @param stepNum
	 * @return
	 */
	private V find(int startPos, String key, int stepNum) {//worst Performance at O(n) best is O(1).
Pair obj = (Pair) arr[startPos];//Here we are creating a new obj that stores both the key and value that is within startPos.
if(obj == null){// This statement will check whether that the current obj exists. i.e if the key and value are null, this can mean that the key is not in the array.
	return null;//If the object is null then it will return null which means that it doesn't exist. This can be considered the best time performance of constant time.
}else if(obj.key.equals(key)){//This will just pull and use the key to compare it with the given key within that position , if they are the same then it return the value.
	return obj.value;
}else{//Else if the key didn't match it will then find a new start position.
	startPos = getNextLocation(startPos, ++stepNum, key); //getNextLocation performs at linear time which is the worst case.
	return find(startPos,key,stepNum);//Recursive call to repeat method. This call the method to perform again with a new start position.
}
	}
	/**
	 * Find the first unoccupied location where a value associated with key can be stored, starting the
	 * search at position startPos. If startPos is unoccupied, return startPos. Otherwise use the getNextLocation
	 * method with an incremented value of stepNum to find the appropriate next position to check
	 * (which will differ depending on the probe type being used) and use this in a recursive call to findEmpty.
	 * @param startPos
	 * @param stepNum
	 * @param key
	 * @return
	 */
	private int findEmpty(int startPos, int stepNum, String key) {//worst Performance at O(n) and best performance is at O(1).
		while(arr[startPos] != null){//Will test if there is a null positions to store new keys and values. This will performance at linear time O(n).
			startPos = getNextLocation(startPos, ++stepNum,key);//If the position within the array doesn't equal null then startPos will be equalled the getNextLocation method which finds a new position to test if null.
		}
		return startPos; //When a null position is found it will return the position as startpos. This will perform at constant time being the best performance.
	}
	/**
	 * Finds the next position in the Hashtable array starting at position startPos. If the linear
	 * probe is being used, we just increment startPos. If the double hash probe type is being used,
	 * add the double hashed value of the key to startPos. If the quadratic probe is being used, add
	 * the square of the step number to startPos.
	 * @param
	 * @param stepNum
	 * @param key
	 * @return
	 */
	private int getNextLocation(int startPos, int stepNum, String key) {// Performs at linear time overall O(n).
		int step = startPos; //Depending on the probe this will find a new start position that will be used to find both an empty and occupied location within the hashtable.
		switch (probeType) {
		case LINEAR_PROBE:
			step++;
			break;
		case DOUBLE_HASH:
			step += doubleHash(key);
			break;
		case QUADRATIC_PROBE:
			step += stepNum * stepNum;
			break;
		default:
			break;
		}
		return step % max;
	}
	/**
	 * A secondary hash function which returns a small value (less than or equal to DBL_HASH_K)
	 * to probe the next location if the double hash probe type is being used
	 * @param key
	 * @return
	 */
	private int doubleHash(String key) {// This will perform at a time of linear time.
		BigInteger hashVal = BigInteger.valueOf(key.charAt(0) - 96);
		for (int i = 0; i < key.length(); i++) {
			BigInteger c = BigInteger.valueOf(key.charAt(i) - 96);
			hashVal = hashVal.multiply(BigInteger.valueOf(27)).add(c);
		}
		return DBL_HASH_K.subtract(hashVal.mod(DBL_HASH_K)).intValue();
	}
	/**
	 * Return an int value calculated by hashing the key. See the lecture slides for information
	 * on creating hash functions. The return value should be less than max, the maximum capacity
	 * of the array
	 * @param key
	 * @return
	 */
	private int hash(String key) {//This has a time complexity of O(n)
		if(key == null){ //This tests if the key is null , if the key is null but has a value then it will throw an execption.
			throw new NullPointerException();
		}
		int hash = 0;//hash value starting with 0.
		int prime = 31;//A small prime number.
		int i;
		for(i =0; i < key.length(); i++) {//loops through the key length indexing every character.
		hash = (prime * hash + key.charAt(i)) % arr.length;//Calculating a new hash getting every character and then add it to (primes * hash).
		}
		return hash;//returns the hash function
	}// This hash function was created using different source such as the slides from week 5 and https://www.cpp.edu/~ftang/courses/CS240/lectures/hashing.htm which provided information to better understand the topic.
	/**
	 * Return true if n is prime
	 * @param n
	 * @return
	 */
	/**
	 * This method will be used to determine that our array length is a is
	 * the smallest prime number which is larger than or equal to initial capacity.
	 * to do this i will make a method to determine if the arr is a prime number.
	 * **/
	private boolean isPrime(int n) {//This has a worst time complexity of O(√n) and a best of O(1)
		boolean IsPrime;
		if( n == 2) { // This statement says that if n is equal to 2 then it is true because 2 is a prime number.
			IsPrime = true ; // This is the best performance which is down at constant itme.
		}else if(n % 2 == 0|| n <= 1){ // This statement says that if n is doesn't have a remainder and is equaled or less than one then its not a prime number.
			IsPrime = false;
		}else{
			if(n % 2 != 0); IsPrime = true; // this says that if n doesn't equal 0 then its a prime number.
			for (int i =3; i*i < n ; i +=2) {// This performs at O(√n) which is the worst time for isPrime.
				if( n % i == 0){
					IsPrime = false;
				}
			}
		}
		return IsPrime;//Will return true or false if the length or value n is a prime number.

	}

	/**
	 * Get the smallest prime number which is larger than n
	 * @param n
	 * @return
	 */
	private int nextPrime(int n) {//This has a time complexity of O(√n) overall
		if (n % 2 == 0){// if n leaves a remainder then it is even so plus 1 to n to make it odd.
			n++;//increment n.
		}
		while(isPrime(n) == false){// this calls the prime number method above to check if n is a prime number now. This will perform at O(√n) because isPrime is called.
			n +=2;// if n is not a prime number then increment by 2 so it can search all the odd values above n for the smallest prime number.
		}
			return n; //will return the next smallest prime number.
	}
	/**
	 * Resize the hashtable, to be used when the load factor exceeds maxLoad. The new size of
	 * the underlying array should be the smallest prime number which is at least twice the size
	 * of the old array.
	 * @param
	 */
	private int  resize(Object[] arr) {//This performs at an overall time of O(N^2)
		int n = arr.length*2;//n will be twice the size of the current size.
		if (isPrime(n) != true) { // This calls isPrime method to check that the new size is a prime number. this performs at O(√n) because isPrime is called.
			nextPrime(n);// if n isn't a prime number then it will call nextPrime method to find the next smallest value to set n. This performs at O(√n).
		}
		max = n;//We are setting max to be equal to n as the max is the same as the array length.
		arr = Hashtable.arr;//Making a new temp hashtable.
		Hashtable.arr = new Object[max];//Setting the new hash table to have the size of max.
		int i;
		for(i =0; i < arr.length; i++){//This loop will find all the keys and stores them in the new array.
			if(arr[i] != null){//Will check if a key is present if then pair the key and value and store it in the same position in the new array, else it  then it will carry on looping.
				Pair obj = (Pair)arr[i];//Pairs the key and value as a new object which can be easily stored within the new array.
				put(obj.key, obj.value );//put method will be called to store it.
				//Overall this entire method performs at O(n^2) becuase of the for loop and the call of method put.
			}
		}
		return n;//Returns the new length and new array with new length.
	}
	/**
	 * Instances of Pair are stored in the underlying array. We can't just store
	 * the value because we need to check the original key in the case of collisions.
	 * @author jb259
	 *
	 */
	private class Pair {
		private String key;
		private V value;

		public Pair(String key, V value) {
			this.key = key;
			this.value = value;
		}
	}

}