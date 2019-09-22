package com.flyzebra.tetris;

import java.util.Random;

public class TetriShape {
	// 位置
	private int left, top, right, bottom;
	// 各种形状
	private int shapes[][][] = {
			{ { 0, 0, 0, 0 }, { 0, 1, 1, 0 }, { 0, 1, 1, 0 }, { 0, 0, 0, 0 } },// 00 正方形
			{ { 0, 0, 1, 0 }, { 0, 0, 1, 0 }, { 0, 0, 1, 0 }, { 0, 0, 1, 0 } },// 01 长条1
			{ { 0, 0, 0, 0 }, { 0, 1, 0, 0 }, { 0, 1, 0, 0 }, { 0, 1, 1, 0 } },// 02 L形1
			{ { 0, 0, 0, 0 }, { 0, 0, 1, 0 }, { 0, 0, 1, 0 }, { 0, 1, 1, 0 } },// 03 反L形1
			{ { 0, 0, 0, 0 }, { 0, 1, 1, 0 }, { 0, 0, 1, 1 }, { 0, 0, 0, 0 } },// 04 Z形
			{ { 0, 0, 0, 0 }, { 0, 0, 1, 1 }, { 0, 1, 1, 0 }, { 0, 0, 0, 0 } },// 05 反Z形
			{ { 0, 0, 0, 0 }, { 0, 0, 1, 0 }, { 0, 1, 1, 1 }, { 0, 0, 0, 0 } },// 06 山形1
			
			{ { 0, 0, 0, 0 }, { 0, 1, 1, 0 }, { 0, 1, 1, 0 }, { 0, 0, 0, 0 } },// 07 正方形2
			{ { 0, 0, 0, 0 }, { 0, 0, 0, 0 }, { 1, 1, 1, 1 }, { 0, 0, 0, 0 } },// 08 长条2
			{ { 0, 0, 0, 0 }, { 0, 1, 1, 1 }, { 0, 1, 0, 0 }, { 0, 0, 0, 0 } },// 09 L形2
			{ { 0, 0, 0, 0 }, { 0, 1, 0, 0 }, { 0, 1, 1, 1 }, { 0, 0, 0, 0 } },// 10 反L形2
			{ { 0, 0, 0, 0 }, { 0, 0, 1, 0 }, { 0, 1, 1, 0 }, { 0, 1, 0, 0 } },// 11 Z形2
			{ { 0, 0, 0, 0 }, { 0, 1, 0, 0 }, { 0, 1, 1, 0 }, { 0, 0, 1, 0 } },// 12 反Z形2
			{ { 0, 0, 0, 0 }, { 0, 0, 1, 0 }, { 0, 0, 1, 1 }, { 0, 0, 1, 0 } },// 13 山形2
			
			{ { 0, 0, 0, 0 }, { 0, 1, 1, 0 }, { 0, 1, 1, 0 }, { 0, 0, 0, 0 } },// 14 正方形3
			{ { 0, 0, 1, 0 }, { 0, 0, 1, 0 }, { 0, 0, 1, 0 }, { 0, 0, 1, 0 } },// 15 长条3
			{ { 0, 0, 0, 0 }, { 0, 1, 1, 0 }, { 0, 0, 1, 0 }, { 0, 0, 1, 0 } },// 16 L形3
			{ { 0, 0, 0, 0 }, { 0, 1, 1, 0 }, { 0, 1, 0, 0 }, { 0, 1, 0, 0 } },// 17 反L形3
			{ { 0, 0, 0, 0 }, { 0, 1, 1, 0 }, { 0, 0, 1, 1 }, { 0, 0, 0, 0 } },// 18 Z形3
			{ { 0, 0, 0, 0 }, { 0, 0, 1, 1 }, { 0, 1, 1, 0 }, { 0, 0, 0, 0 } },// 19 反Z形3
			{ { 0, 0, 0, 0 }, { 0, 0, 0, 0 }, { 0, 1, 1, 1 }, { 0, 0, 1, 0 } },// 20 山形3
			
			{ { 0, 0, 0, 0 }, { 0, 1, 1, 0 }, { 0, 1, 1, 0 }, { 0, 0, 0, 0 } },// 21 正方形4
			{ { 0, 0, 0, 0 }, { 0, 0, 0, 0 }, { 1, 1, 1, 1 }, { 0, 0, 0, 0 } },// 22 长条4
			{ { 0, 0, 0, 0 }, { 0, 0, 0, 1 }, { 0, 1, 1, 1 }, { 0, 0, 0, 0 } },// 23 L形4
			{ { 0, 0, 0, 0 }, { 0, 1, 1, 1 }, { 0, 0, 0, 1 }, { 0, 0, 0, 0 } },// 24 反L形4
			{ { 0, 0, 0, 0 }, { 0, 0, 1, 0 }, { 0, 1, 1, 0 }, { 0, 1, 0, 0 } },// 25 Z形4
			{ { 0, 0, 0, 0 }, { 0, 1, 0, 0 }, { 0, 1, 1, 0 }, { 0, 0, 1, 0 } },// 26 反Z形4
			{ { 0, 0, 0, 0 }, { 0, 0, 1, 0 }, { 0, 1, 1, 0 }, { 0, 0, 1, 0 } } // 27 山形4
	};
	private int shape[][] = new int[4][4];
	private int shape_cret, shape_rote;

	public TetriShape() {
		left = 3;
		top = -4;
		right = 6;
		bottom = -1;
		Random random = new Random();
		int i = random.nextInt();
		if (i < 0) {
			i = 0 - i;
		}
		shape_cret = i % 28;
		if(shape_cret==13){
			left=2;
			right=5;
		}
		shape = shapes[shape_cret].clone();
	}

	public int getShape_cret() {
		return shape_cret;
	}

	public int[][] getRotShape() {
		shape_rote = (shape_cret + 7) % 28;
		return shapes[shape_rote].clone();
	}

	public int getLeft() {
		return left;
	}

	public int getTop() {
		return top;
	}

	public int getRight() {
		return right;
	}

	public int getBottom() {
		return bottom;
	}

	public int[][] getShape() {
		return shape;
	}

	public synchronized void setShape(int[][] shape) {
		shape_cret = shape_rote;
		this.shape = shape;
	}

	public synchronized void setPosition(int left, int top, int right,
			int bottom) {
		this.left = left;
		this.top = top;
		this.right = right;
		this.bottom = bottom;
	}
}
