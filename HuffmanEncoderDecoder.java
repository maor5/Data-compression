
/**
 * Assignment 1
 * Submitted by: 
 * Student 1.maor hadad 	ID# 308201565
 * Student 2.adi hadar	ID# 315748749
 */
package Huffman_Algo;

import java.io.*;

/**
 * Assignment 1
 * Submitted by: 
 * Student 1.maor hadad 	ID# 308201565
 * Student 2. adi hadar		ID# 315748749
 */
import java.util.PriorityQueue;

import Huffman_Algo.list;
import interface_for_huffman.Compressor;

import java.lang.String;


public class HuffmanEncoderDecoder implements Compressor{
	
	public HuffmanEncoderDecoder(){
	}
	
	public node create_hufman_tree(int []freq) {
		PriorityQueue<node> priorityQueue=new PriorityQueue<node>();
		for(int i=0;i<256;i++) {
			if(freq[i]>0) {
				node x=new node(i, freq[i], null, null);
				priorityQueue.add(x);
				
			}
		}
		while(priorityQueue.size()>1) {
			node left=priorityQueue.poll();
			node right =priorityQueue.poll();
			
			node dad=new node('\0', left.frequency+right.frequency , left, right);//connect as a tree
			priorityQueue.add(dad);
		}
		node root_hufman_tree=priorityQueue.poll();
		return root_hufman_tree;	
	}
	
	public void postOrder(node node,String str,String[]s) {
		if(node.left !=  null && node.right!=null) {  //only the leaf
			postOrder(node.left,str+"0",s);
			postOrder(node.right,str+"1",s);	
		}
		else {
			s[node.ch]=str;
		}
	}
	
	public void Compress(String[] input_names, String[] output_names){
		System.out.println("\nstart Huffman compressing...");
		FileInputStream input = null;
		FileOutputStream output=null;
		try {
			input = new FileInputStream(input_names[0]);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		 try {
			output = new FileOutputStream(output_names[0]);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		list list=new list();
		 
		final int[]freq = new int[256];
		for(int i=0;i<256;i++) {
			freq[i]=0;
		}
		
		
		System.out.println("countning frequency...");
		int ascii_char = -2;
		int num_of_chars_in_the_file=0;
		while(true) 
		{
			try {
				ascii_char = input.read();
				if (ascii_char != -1) {
				list.insert_from_back(ascii_char);
				num_of_chars_in_the_file++;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (ascii_char != -1) {
				freq[ascii_char]++;
			}
			else {
				break;	
			}
		}
		
		
		//A number greater than 255 will change as we eject it into a file 
		//so we save it in the number of times that 255 is divided and the remainder is divided by 255 And write them to the outfile 
		int num_of_times_divide_by_255_power_2=0;
		int num_of_times_divide_by_255=0;
		int reminder=0;
		int tmp_num;
		if(num_of_chars_in_the_file>(255*255)) {
			num_of_times_divide_by_255_power_2=num_of_chars_in_the_file/(255*255);
			tmp_num=num_of_chars_in_the_file%(255*255);
			num_of_times_divide_by_255=tmp_num/255;
			reminder=tmp_num%255;
		}
		else {
			num_of_times_divide_by_255 = num_of_chars_in_the_file/255;
			reminder=num_of_chars_in_the_file % 255;	
		}
		
		try {
			output.write(num_of_times_divide_by_255_power_2);
			output.write(num_of_times_divide_by_255);
			output.write(reminder);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		//Same for the probabilities
		for(int i=0;i<256;i++) {
			num_of_times_divide_by_255_power_2=0;
			num_of_times_divide_by_255=0;
			reminder=0;
			tmp_num=0;
			if(freq[i]>(255*255)) {
				num_of_times_divide_by_255_power_2=freq[i]/(255*255);
				tmp_num=freq[i]%(255*255);
				num_of_times_divide_by_255=tmp_num/255;
				reminder=tmp_num%255;
			}
			else {
				num_of_times_divide_by_255 = freq[i]/255;
				reminder=freq[i] % 255;	
			}
			try {
				output.write(num_of_times_divide_by_255_power_2);
				output.write(num_of_times_divide_by_255);
				output.write(reminder);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

		//call the func to get the huf_tree
		System.out.println("creating hufman tree...");
		node root_hufman_tree_from_function=create_hufman_tree(freq);
		
		//only one letter
		if(root_hufman_tree_from_function.left ==  null && root_hufman_tree_from_function.right== null) {
			System.out.println("we work only with 3 or above");
		}
		String[]char_code=new String[256]; 
		System.out.println("run on the huffman tree postorder....");
		postOrder(root_hufman_tree_from_function, "", char_code);
		list_node current_node=list.head;
		String str_8_bits="";
		int i=0;
		int flag=0;
		while(current_node!=null){
			try {
				while(str_8_bits.length()<8 && current_node!=null){	
					 str_8_bits=str_8_bits+char_code[current_node.data].charAt(i);
					 i++;
					 if(char_code[current_node.data].length()==i) {//finish string
						 current_node=current_node.next;
						 i=0;// to make the pointer to point on the 0 position next string 
					 }
				}
				while(str_8_bits.length()<8) {
					str_8_bits+="0";
				}
				int printed_num = Integer.parseInt(str_8_bits,2);// convert the string to a number to send to the out file
				output.write(printed_num);
				str_8_bits="";
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
		}
		
		System.out.println("Huffman compressed succsessfuly!!\n ");
	}
	
	public void Decompress(String[] input_names, String[] output_names){
		System.out.println("start Huffman decompressing.........................");
		 FileInputStream input = null;
		 FileOutputStream output=null;
		 int num_of_chars=0;
		 int []freq=new int[256];
		 
		 try {
			input = new FileInputStream(input_names[0]);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
			try {
				output = new FileOutputStream(output_names[0]);
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		int num_of_times_divide_by_255_power_2=0;
		int num_of_times_divide_by_255=0;
		int reminder=0;
		 try {
			 num_of_times_divide_by_255_power_2=input.read();
			 num_of_times_divide_by_255=input.read();
			 reminder=input.read();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 num_of_chars= num_of_times_divide_by_255_power_2*255*255+num_of_times_divide_by_255*255+reminder;	
		for(int i=0;i<256;i++) {
			try {
				num_of_times_divide_by_255_power_2=input.read();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			 try {
				num_of_times_divide_by_255=input.read();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 try {
				reminder=input.read();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 freq[i]= num_of_times_divide_by_255_power_2*255*255+num_of_times_divide_by_255*255+reminder;

		 }
		System.out.println("creating the huffman tree again....");
		node head_hofman_tree=create_hufman_tree(freq);
		node current_ptr_hofman_tree=head_hofman_tree;	
		
		int num_final=-2;//inilize
		int count_of_writed_char=0;
		int arr_to_opposite_the_bits[]=new int[8];
		int bit1=-1;//inilize
		
		while(true) {
			try {
				num_final=input.read();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if(num_final!=-1) {
				//those 2 fors are for take the left bit from the "8 bits" by create an array and pass it 2 times
				//to oposite the bits
				for(int j=0;j<8;j++) {
					bit1=num_final%2;
					arr_to_opposite_the_bits[j]=bit1;
					num_final=(num_final/2);
				}
				
				for(int i=7;i>=0;i--) {
					int bit=arr_to_opposite_the_bits[i];
					if(current_ptr_hofman_tree.left!=null && current_ptr_hofman_tree.right!=null) {
						if(bit==0) {
							current_ptr_hofman_tree=current_ptr_hofman_tree.left;
							if(current_ptr_hofman_tree.left==null && current_ptr_hofman_tree.right==null) {
								try {
									output.write(current_ptr_hofman_tree.ch);
								} catch (IOException e) {
									e.printStackTrace();
								}
								current_ptr_hofman_tree=head_hofman_tree;
								count_of_writed_char++;
								if(num_of_chars==count_of_writed_char)
								break;
							}
						}
						else {
							current_ptr_hofman_tree=current_ptr_hofman_tree.right;
							if(current_ptr_hofman_tree.left==null && current_ptr_hofman_tree.right==null) {
								try {
									output.write(current_ptr_hofman_tree.ch);
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								current_ptr_hofman_tree=head_hofman_tree;
								count_of_writed_char++;
								if(num_of_chars==count_of_writed_char)
									break;
							}
						}
					}
				}
			}
			else {
				break;
			}			
		}
		System.out.println("Huffman decompressed succsessfuly!!");
	}
	
	public byte[] CompressWithArray(String[] input_names, String[] output_names)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public byte[] DecompressWithArray(String[] input_names, String[] output_names)
	{
		// TODO Auto-generated method stub
		return null;
	}
}