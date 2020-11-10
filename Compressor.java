package interface_for_huffman;

import Huffman_Algo.node;

public interface Compressor {
	abstract public void Compress(String[] input_names, String[] output_names);
	abstract public void Decompress(String[] input_names, String[] output_names);

	abstract public byte[] CompressWithArray(String[] input_names, String[] output_names);
	abstract public byte[] DecompressWithArray(String[] input_names, String[] output_names);
	public void postOrder(node node,String str,String []s);
	public node create_hufman_tree(int []freq);

}
