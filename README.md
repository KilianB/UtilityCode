# UtilityCode

[![Build Status](https://travis-ci.org/KilianB/UtilityCode.svg?branch=master)](https://travis-ci.org/KilianB/UtilityCode)
[ ![Download](https://api.bintray.com/packages/kilianb/maven/UtilityCode/images/download.svg) ](https://bintray.com/kilianb/maven/UtilityCode/_latestVersion)

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
			<version>1.2.0</version>
		</dependency>
	</dependencies>
```

<details>
<summary>Gradle</summary>
<code>compile 'com.github.kilianB:UtilityCode:1.0.0'</code>
</details>

## Content

<table>
	<tr>
		<th></th>
		<th></th>
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
		<td>Center text, get length of integers ....</td>
	</tr>
	<tr>
		<td>Graphics</td>
		<td>Color Utilities, Distance, Average Color, Palettes</td>
	</tr>
	<tr>
		<td>ArrayUtil</td>
		<td>Fill, search validation, deep clone</td>
	</tr>
	<tr>
		<td>CryptoUtil</td>
		<td>AES en- and decryption</td>
	</tr>
	<tr>
		<td>MathUtil</td>
		<td>clamping, fractional part, double compare</td>
	</tr>
	<tr>
		<td>...</td>
		<td>Non daemon thread factory, circular hashmap, rescaling images, ...</td>
	</tr>
</table>

## Changelog

````

## [unreleased]

### [added]
- FastPixel utility to read rgb/hue/luma data from an image
- MathUtil.isNumeric() + tests

### [fixed] 
- added @Nested tag to isDoubleEquals class in MathUtil test

## v 1.2.0 - 23.10.2018
### [added]:

 - fillArrayMulti in ArrayUtils allows multi dimensional arrays to be recursively filled with a value
 - test for ArrayUtil

 - Mutable wrappers now extend Number if applicable
 - Mutable now implement increment, decrement setValue for primitive values. 
 - test for mutable


## v 1.1.0 - 19.10.2018
### [added]:
 - CountHashCollection + test A hash collection with O(1) specialized on keeping track how many times equal object were added and removed
 - Circular Hashset
 - Circular Queue
 - Require.inRange for collections
 ### [changed]:
 - replace DaemonThreadFactory by NamedThreadFactory since no external repo is currently depending on this source only bump minor version
