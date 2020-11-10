package Huffman_Algo;

public class node implements Comparable<node>{
	public int frequency;
	public int ch;
	public node left;
	public node right;

	public node(int ch,int frequency,node left, node right) {
		this.ch=ch;
		this.frequency=frequency;
		this.left=left;
		this.right=right;
	}
	boolean isLeaf() {
		return this.left==null && this.right==null;
	}
	@Override
	public int compareTo(node o) {
		int freq_comper = Integer.compare(this.frequency, o.frequency);
		if(freq_comper!=0) {
			return freq_comper;
		}
		return Integer.compare(this.ch,o.ch);
	}
}
