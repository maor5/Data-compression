
import java.io.IOException;

import Huffman_Algo.HuffmanEncoderDecoder;
public class main
{
	public static boolean is_LZW_effective(double avg_of_long_repit,int num_of_repitation,int num_of_chars_in_file) {//Of course this is not binding but only a kind of hypothesis / indication
			double mul=avg_of_long_repit*num_of_repitation;
		if(num_of_chars_in_file>mul) {
				return false;
			}
			else
				return true;
		}

	static String IN_FILE_PATH = "C:\\Users\\rel13\\Desktop\\הורדות\\ExampleInputs\\Smiley.bmp";
	static String lzw_comp = "C:\\Users\\rel13\\Desktop\\הורדות\\ExampleInputs\\1.bmp";
	static String huff_comp = "C:\\Users\\rel13\\Desktop\\הורדות\\ExampleInputs\\2.bmp";
	static String huff_decomp = "C:\\Users\\rel13\\Desktop\\הורדות\\ExampleInputs\\3.bmp";
	static String lzw_decomp = "C:\\Users\\rel13\\Desktop\\הורדות\\ExampleInputs\\5.bmp";

	public static void main(String[] args) throws IOException
	
	{
		
		String[] input_names=new String[1];
		String[] lzw_compress=new String[1];
		String[] huff_compress=new String[1];
		String[] huff_decompress=new String[1];
		String[] lzw_decompress=new String[1];
		input_names[0]=IN_FILE_PATH;
		lzw_compress[0]=lzw_comp;
		huff_compress[0]=huff_comp;
		huff_decompress[0]=huff_decomp;
		lzw_decompress[0]=lzw_decomp;
		
		lzw_compress ob_lzw=new lzw_compress();
		HuffmanEncoderDecoder ob_huff=new HuffmanEncoderDecoder();
		
		ob_lzw.Compress(input_names, lzw_compress);
		ob_huff.Compress(lzw_compress, huff_compress);
		ob_huff.Decompress(huff_compress, huff_decompress);
		ob_lzw.Decompress(huff_decompress, lzw_decompress);
		
		System.out.println("\nimportant information for this file: ");
		System.out.println("the num of chars in the file is: "+ob_lzw.num_of_chars_in_org_file);
		System.out.println("the time of repittions is: "+ob_lzw.count_the_repetition);
		System.out.println("Average the length of the repeating string: "+ob_lzw.avg_str_long);
		
		if(is_LZW_effective(ob_lzw.avg_str_long,ob_lzw.count_the_repetition, ob_lzw.num_of_chars_in_org_file)) {//according to a formula that we create.
			System.out.println("LZW is effective for this file!");
		
		}
		else {// lzw not so effective(consider the time of the algo as well)
			System.out.println("lzw not so effective for this file!");
		}
		

	}
}

