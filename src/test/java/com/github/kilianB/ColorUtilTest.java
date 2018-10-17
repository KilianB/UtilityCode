package com.github.kilianB;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.github.kilianB.graphics.ColorUtil;

import javafx.scene.paint.Color;

class ColorUtilTest {

	@Nested
	class AwtToFX{
		@Test
		void black() {
			assertEquals(javafx.scene.paint.Color.BLACK,
					ColorUtil.awtToFxColor(java.awt.Color.BLACK));
		}
		
		@Test
		void white() {
			assertEquals(javafx.scene.paint.Color.WHITE,
					ColorUtil.awtToFxColor(java.awt.Color.WHITE));
		}
		
		@Test
		void gray() {
			assertEquals(javafx.scene.paint.Color.GRAY,
					ColorUtil.awtToFxColor(java.awt.Color.GRAY));
		}
		
		@Test
		void test() {
			javafx.scene.paint.Color fxColor = javafx.scene.paint.Color.web("#961e1e");
			
			java.awt.Color awtColor = java.awt.Color.decode("#961e1e");
			
			javafx.scene.paint.Color genfxColor = ColorUtil.awtToFxColor(awtColor);
			assertEquals(fxColor,genfxColor);
		}
		
		@Test
		@Disabled
		void alpha() {
			//javafx.scene.paint.Color.web(colorString, opacity)
			// java.awt.Color.decode(nm) what is the awt equivalent?
			//DON'T compare float and doubles!
			assertEquals(new javafx.scene.paint.Color(0.5d,0.5d,0.5d,0.3d),
					ColorUtil.awtToFxColor(new java.awt.Color(0.5f,0.5f,0.5f,0.3f)));
		}
		
	}
	
	@Nested
	class FXToAwt{
		@Test
		void black() {
			assertEquals(java.awt.Color.BLACK,
					ColorUtil.fxToAwtColor(javafx.scene.paint.Color.BLACK));
		}
		
		@Test
		void white() {
			assertEquals(java.awt.Color.WHITE,
					ColorUtil.fxToAwtColor(javafx.scene.paint.Color.WHITE));
		}
		
		@Test
		void gray() {
			assertEquals(java.awt.Color.GRAY,
					ColorUtil.fxToAwtColor(javafx.scene.paint.Color.GRAY));
		}
		
		@Test
		void test() {
			javafx.scene.paint.Color fxColor = javafx.scene.paint.Color.web("#961e1e");
			
			java.awt.Color awtColor = java.awt.Color.decode("#961e1e");
			
			java.awt.Color genAwtColor = ColorUtil.fxToAwtColor(fxColor);
			assertEquals(awtColor,genAwtColor);
		}
		
		@Test
		@Disabled
		void alpha() {
			//javafx.scene.paint.Color.web(colorString, opacity)
			// java.awt.Color.decode(nm) what is the awt equivalent?
			
			//DON'T compare float and doubles!
			assertEquals(new javafx.scene.paint.Color(0.5d,0.5d,0.5d,0.3d),
					ColorUtil.awtToFxColor(new java.awt.Color(0.5f,0.5f,0.5f,0.3f)));
		}
	}
	
	@Nested
	class ColorDistance{
		
		@Test
		void symmetry() {
			javafx.scene.paint.Color c1 = javafx.scene.paint.Color.GREENYELLOW;
			javafx.scene.paint.Color c2 = javafx.scene.paint.Color.DARKORANGE;
			assertEquals(ColorUtil.distance(c1, c2),ColorUtil.distance(c2, c1));
		}
		
		@Test
		void identity() {
			javafx.scene.paint.Color c1 = javafx.scene.paint.Color.DARKORANGE;
			javafx.scene.paint.Color c2 = javafx.scene.paint.Color.DARKORANGE;
			assertEquals(0,ColorUtil.distance(c2, c1));
		}
		
		@Test
		void distance() {
			javafx.scene.paint.Color c1 = javafx.scene.paint.Color.WHITE;
			javafx.scene.paint.Color c2 = javafx.scene.paint.Color.DARKORANGE;
			javafx.scene.paint.Color c3 = javafx.scene.paint.Color.ORANGE;
			assertTrue(ColorUtil.distance(c1, c2) > ColorUtil.distance(c2, c3));
		}
	}

	@Nested
	class ContrastColor{
		@Test
		void white(){
			assertEquals(Color.BLACK,ColorUtil.getContrastColor(Color.WHITE));
		}
		
		@Test
		void black(){
			assertEquals(Color.WHITE,ColorUtil.getContrastColor(Color.BLACK));
		}
		
		@Test
		void yellow(){
			assertEquals(Color.BLACK,ColorUtil.getContrastColor(Color.YELLOW));
		}
		
		@Test
		void blue(){
			assertEquals(Color.WHITE,ColorUtil.getContrastColor(Color.BLUE));
		}
	}
}