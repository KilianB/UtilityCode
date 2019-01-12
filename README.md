# UtilityCode

[![Build Status](https://travis-ci.org/KilianB/UtilityCode.svg?branch=master)](https://travis-ci.org/KilianB/UtilityCode)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/39c6aaf496e34cd9ac796d347e2c4dde)](https://www.codacy.com/app/KilianB/UtilityCode?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=KilianB/UtilityCode&amp;utm_campaign=Badge_Grade)
[![Download](https://api.bintray.com/packages/kilianb/maven/UtilityCode/images/download.svg) ](https://bintray.com/kilianb/maven/UtilityCode/_latestVersion)

Selection of java utility methods used in almost all projects.

## Maven, Gradle, Ivy

Distributed via Bintray 
``` XML
	<repositories>
		<repository>
			<id>jcenter</id>
			<url>https://jcenter.bintray.com/</url>
		</repository>
	</repositories>
	
	<dependencies>
		<dependency>
			<groupId>com.github.kilianB</groupId>
			<artifactId>UtilityCode</artifactId>
			<version>1.5.7</version>
		</dependency>
	</dependencies>
```

<details>
<summary>Gradle</summary>
<code>compile 'com.github.kilianB:UtilityCode:1.5.6'</code>
</details>


## Examples


````Java

//Restart JVM
MiscUtil.restartApp();

//Enum hashcode
int enumHashcode = MiscUtil.consistentHashCode(Weekday.Monday);

//Array methods
int[] values = {1,2,1,4,5,7,3,4,3,2};
		
ArrayUtil.median(values);
ArrayUtil.average(values);
ArrayUtil.add(values,5);

...
````

## Content

<table>
	<tr>
		<th>Package</th>
		<th>Description</th>
	</tr>
	<tr>
		<td>MiscUtil</td>
		<td>Get OS info/ Restart Java VM</td>
	</tr>
	<tr>
		<td>Require</td>
		<td>Input assertions similar to <code> Objects.requireNonNull()</code></td>
	</tr>
	<tr>
		<td>NetworkUtil</td>
		<td>Resolve Network adapter and socket helper</td>
	</tr>
	<tr>
		<td>Mutable</td>
		<td>Mutable Wrapper for boxed primitives</td>
	</tr>
	<tr>
		<td>StringUtil</td>
		<td>Center text, get length of numeric string representation. Multiy strings</td>
	</tr>
	<tr>
		<td>Graphics</td>
		<td>Color Utilities, Distance, Average Color, Palettes, Efficient RGB HSC YCrCB (*10 faster than JDK)</td>
	</tr>
	<tr>
		<td>ArrayUtil</td>
		<td>Fill, search, compute sorted indices validation, deep clone, scalar operation on arrays</td>
	</tr>
	<tr>
		<td>CryptoUtil</td>
		<td>AES en- and decryption</td>
	</tr>
	<tr>
		<td>MathUtil</td>
		<td>clamping, fractional part, double compare, log to base x, trangular number </td>
	</tr>
	<tr>
		<td>clustering</td>
		<td>small selection of cluster algorithms. KMeans KMeans++ DBScan, see cluster branch for more. WIP</td>
	</tr>
	<tr>
		<td>...</td>
		<td>Non daemon thread factory, circular hashmap, rescaling images, ...</td>
	</tr>
</table>

## Changelog

## v 1.5.7 - 12.01.2018

### [changed]
- upgraded pcg random dependency to version 1.0.1
- explicitly define javafx dependency in pom

## v 1.5.6 - 08.01.2018

### [added]
- Triplet utility class

### [added]
- median and average methods for array

## v 1.5.5 - 31.12.2018

### [added]
- median and average methods for array

### [fixed]
- include some array util tests which preciously got ignored due to missing annotation
- some bad code patterns.

## v 1.5.4 - 26.12.2018

### [added]
-   Pair data structure
-   codacy badge

### [changed]
- removed unused imports. A tiny bit cleanup
- bump javadoc version to html 5

## v1.5.3 - 25.12.2018

### [changed]

-   use ImageTypeSpecifier to create a compatible buffered image during resizing operation
in image utils. This allows support for buffered image with type custom.
-   increased named thread factory test case exception timeout to make test more robust

## v1.5.2 - 25.12.2018

### [added]

-   Require oddValue,evenValue, greaterThan, lessThan, negativeValue
-   default fallback for fastpixel if no faster implementation is available

### [changed]
-   CountHashCollection now also returns the mapped object in the toString method
-   move some of fastpixels implementation methods to default methods

##   v1.5.1 - 30.11.2018

### [fixed]
 -   perform mcn:clean before deploying to exclude test dependencies from final build.

## v1.5.0 - 29.11.2018

### [added]
 -   getSetAverageGrayScale
 -   utility toString methods for arrays
 -   getLuma 1D version
 -   getLowerShiftBitMask to MathUtil
 -   getConsistentHashcode for Enums in the misc package
 
### [fixed]
 -   getLuma now correctly accesses red green and blue values based on BGR byte array offsets instead of RGB. 

### [changed]
 -   refactored FastPixel to use base class and support multiple different image types
 
## v1.4.5 - (intermediate internal version)

### [added]
 -   bulk set operation for FastPixel class

### [fixed]
 -   setRed,setBlue,setGreen on FastPixel now access the correct offset

## v1.4.4 - 19.11.2018 (intermediate internal version)

### [added]
 -   pairwise array math (add subtract multiply divide)
 -   fillArray overloaded now available with function parameter passing the index to a supplier method
 -   getSortedIndex. Calculate the sorted indices of an array in ascending or descending order.
 
### [changed]
 -   make constructor of circular hashset private. Class was not ever meant to be initialized

## v.1.4.2
### [fixed] 
 -   getScaledInstance of images not correctly uses drawImage to rescale rather
 than falling back to the awt getScaledInstance call. 600 fold increase in performance


## v.1.4.1 - 15.11.2018

### [added]
-   triangular number 

## v 1.4.0 - 14.11.2018

### [fixed]
-   javadoc get hue is in range of [0-360]° not [0-255]

### [added]
-   getContrastColor for awt images
-   getLuma for awt and fxImages
-   log(base,value) calculate log of an arbitrary base
-   plain autocloseable (AutoCloseable without throwing an exception)
-   get maximum and minimum as well as maximum and minimum Index for primitive numeric arrays
-   scalar multiplication, addition, subtraction and division for primitive numeric arrays.
-   Clustering. KMeans, KMeans++, DBScan, CURE, DistanceMeasures, Euclidiean Jaccard Manhattan
-   StringUtils fillStringBeginning

## v 1.3.1 - 2.11.2018

### [added]
-   getLuma() array method

## v 1.3.0 - 2.11.2018

### [added]
-   FastPixel utility to read rgb/hsv/YCrCb data from an image + tests
-   MathUtil.isNumeric() + tests

### [fixed] 
-   added @Nested tag to isDoubleEquals class in MathUtil test

## v 1.2.0 - 23.10.2018
### [added]:

 -   fillArrayMulti in ArrayUtils allows multi dimensional arrays to be recursively filled with a value
 -   test for ArrayUtil

 -   Mutable wrappers now extend Number if applicable
 -   Mutable now implement increment, decrement setValue for primitive values. 
 -   test for mutable

## v 1.1.0 - 19.10.2018
### [added]:
 -   CountHashCollection + test A hash collection with O(1) specialized on keeping track how many times equal object were added and removed
 -   Circular Hashset
 -   Circular Queue
 -   Require.inRange for collections
 ### [changed]:
 -   replace DaemonThreadFactory by NamedThreadFactory since no external repo is currently depending on this source only bump minor version
