
import java.io.FileInputStream;
import Huffman_Algo.list;
import Huffman_Algo.list_node;
import java.io.FileOutputStream;

import java.io.IOException;
public class lzw_compress {
	public String write_to_file="";
	public boolean check_if_dictionary_change_first_time=false;
	public String reminder="";
	public int count_the_repetition=0;
	public double avg_str_long=0;
	public int final_num_of_chars=0;
	public int num_of_chars_in_org_file=0;
	public String first_of_sec_val="";//in decompress section when the value is more then 255 we need to add to the dictionary only the first char
	
	public double find_avg_long_repit(int[]arr) {
		int sum=0;
		int i=0;
		for(i=0;i<arr.length;i++) {
			if(arr[i]==0)
				break;
			sum+=arr[i];
		}
		return sum/i;
	}
	public int find_index_by_str(String []update_ascii,String str,int count_the_time_of_pushing_to_dictionary,int index_save_time) {
		
		 for(int i=index_save_time;i<(256+count_the_time_of_pushing_to_dictionary);i++) {
		
			  if(str.equals(update_ascii[i])) {
				  return i;
				}
			 
		}
		return 0;	
	}
	public int get_num_of_bits(int num) {
		String str=Integer.toBinaryString(num);
		if(str.length()<8)
			return 8;
		return str.length();
		
	}
	
	public void send_bit_by_bit_to_write(FileOutputStream output,int num,int num_of_bits)throws IOException {
		
		String str=Integer.toBinaryString(num);
		while(str.length()<num_of_bits)
			str="0"+str;
		String bit="";
		while(!str.equals("")) {
			bit=str.substring(0,1);
			if(this.write_to_file.length()==8) {
				output.write(Integer.parseInt(this.write_to_file, 2));
				this.write_to_file=bit;
			}
			else {
				this.write_to_file+=bit;
			}
			str=str.substring(1);
		}
		if(this.write_to_file.length()==8) {
			output.write(Integer.parseInt(write_to_file, 2));
			this.write_to_file="";
		}
		
	}
	
	public String complete_8_by_adding_zero_at_right(String str) {
		while(str.length()<8) {
			str+="0";
		}
		return str;
		}
	public String complete_8_by_adding_zero_at_left(String str) {
		while(str.length()<8) {
			str="0"+str;
		}
		return str;
		}
	
	public String read_func(FileInputStream input,int mount_of_bits_to_read)throws IOException  {
		
		String str_to_return="";
		String str_read="";
		str_to_return+=this.reminder;
		this.reminder="";
		
		while(str_to_return.length()<mount_of_bits_to_read) {
			
			str_read=Integer.toBinaryString(input.read());
			while(str_read.length()<8) {
				str_read="0"+str_read;
			}
			while(!str_read.equals("")) {
				if(str_to_return.length()==mount_of_bits_to_read)
					break;
				str_to_return+=str_read.substring(0,1);
				str_read=str_read.substring(1);
			}
			
		}
		this.reminder=str_read;
		return str_to_return;

		
	}
	
	public char[] separate_arrSTR_to_arrCHAR(String[] str) {
		char [] ch=new char[this.final_num_of_chars];
		int index_char_array=0;
		for(int i=0;i<str.length;i++) {
			if(str[i].equals(""))
				break;
			while(str[i].length()>0) {
				ch[index_char_array]=str[i].charAt(0);
				index_char_array++;
				str[i]=str[i].substring(1);
			}
		
		}
		return ch;
		
	}	

	public void Compress(String[] input_names, String[] output_names) throws IOException{
	
	System.out.println("start LZW compressing......");	
	FileInputStream input = null;
	FileOutputStream output=null;
	input = new FileInputStream(input_names[0]);
	output = new FileOutputStream(output_names[0]);
	list list=new list();
	int ascii_char = -2;
	int num_of_aupdating_chars=0;
	int longest_bits_in_dictionary=0;
	int num_of_chars_in_the_file=0;
	int list_size=0;
	String [] out_from_algo;
	String [] dictionary_ascii;
	while(true) 
		{
				ascii_char = input.read();
				if (ascii_char != -1) {
					list.insert_from_back(ascii_char);
					num_of_chars_in_the_file++;
				}
			
			if (ascii_char == -1) {
				break;	
			}
			
		}
		this.final_num_of_chars=num_of_chars_in_the_file;
	
	

	    
	    	out_from_algo=new String[num_of_chars_in_the_file];
	
	    
		int index_of_out_from_algo=0;
		boolean check_if_dictionary_change=false;
		list_node cur_ptr= list.head;
		list_node saver_ptr= list.head;
		int first;
		int second=0;
		dictionary_ascii =new String[256+num_of_chars_in_the_file];//max size - that could be- in the worst case that will be no returns
		
		System.out.println("\ncreating LZW new dictionary......");	
		for(int i=0;i<256;i++) {
			dictionary_ascii[i]=String.valueOf((char)i); //enter to the dictionary the all ascii chars	
			
		}
		String str_combine_firstV_secondV="";
		int count_the_time_of_pushing_to_dictionary=0;
		int index_after_255=256;
		int index=0;
		int [] long_str_repit=new int[num_of_chars_in_the_file];//count the long of every repit
		for(int i=0;i<num_of_chars_in_the_file;i++) {
			long_str_repit[i]=0;
		}
		int index_long_str_repit=0;
		boolean one_char=true;
		boolean extream_case=false;
		int count=0;
		while(cur_ptr.next!=null) {
			count++;
			one_char=false;
			first=cur_ptr.data;
			saver_ptr=cur_ptr;
			String s_extream_case="";
			if(cur_ptr.next.data ==256 && list_size>=3 && count==1) {
				extream_case=true;
				s_extream_case=String.valueOf((char)cur_ptr.data);
			}
				
			cur_ptr=cur_ptr.next;
			second=cur_ptr.data;	
			String first_value="";
			String second_value="";
			if(first>255)
				first_value=dictionary_ascii[first];
			else
				first_value=String.valueOf((char)first);
			if(second>255) {
				if(second==256) {
					if(!extream_case)
						this.first_of_sec_val=String.valueOf(dictionary_ascii[second].charAt(0));
					else
						this.first_of_sec_val=s_extream_case;
				}
				else {
					second_value=dictionary_ascii[second];
					this.first_of_sec_val=String.valueOf(dictionary_ascii[second-1].charAt(dictionary_ascii[second-1].length()-1));

				}
			}
			else {
				second_value=String.valueOf((char)second);
				
			}			
			boolean check_if_once_or_more_found_dictionary=false;
			check_if_dictionary_change=true;
			 index=0;
			int index_save_time=256;//in case that we find some string in the dictionary so next time we want to search from this index 
			int prev_index_in_dic=0;
			 str_combine_firstV_secondV="";
			
			boolean flag=true;
			
			while(saver_ptr.next!=null && flag==true) {
				
				if(check_if_dictionary_change) {
					
					
				   str_combine_firstV_secondV=first_value+second_value;
				}
				index=find_index_by_str(dictionary_ascii,str_combine_firstV_secondV,count_the_time_of_pushing_to_dictionary,index_save_time);
				
					
				if(index!=0) {//check if found the string 
					long_str_repit[index_long_str_repit]++;
					
					count_the_repetition++;
					prev_index_in_dic=index;
					index_save_time=prev_index_in_dic;
					check_if_once_or_more_found_dictionary=true;
					saver_ptr=saver_ptr.next;
					cur_ptr=saver_ptr;
					if(saver_ptr.next!=null) {
						str_combine_firstV_secondV+=String.valueOf(dictionary_ascii[saver_ptr.next.data]);
					    cur_ptr=saver_ptr.next;
					}
					check_if_dictionary_change=false;
				}
				else {
					
					flag=false;
					check_if_dictionary_change=true;
					 dictionary_ascii[index_after_255]=str_combine_firstV_secondV;
					 
					 index_after_255++;
					 count_the_time_of_pushing_to_dictionary++;
					 if(check_if_once_or_more_found_dictionary) {
						 index_long_str_repit++;
							if(longest_bits_in_dictionary<Integer.toBinaryString(prev_index_in_dic).length())
								longest_bits_in_dictionary=Integer.toBinaryString(prev_index_in_dic).length();

							
								out_from_algo[index_of_out_from_algo]=Integer.toBinaryString(prev_index_in_dic);
								index_of_out_from_algo++;
							

						 
						 num_of_aupdating_chars++;
					 }
						 
					 else {
							if(longest_bits_in_dictionary<Integer.toBinaryString(first).length())
								longest_bits_in_dictionary=Integer.toBinaryString(first).length();
							
							
								out_from_algo[index_of_out_from_algo]=Integer.toBinaryString(first);
								index_of_out_from_algo++;
							
							longest_bits_in_dictionary=8;
						 index_save_time=256;//we don't have the indication so we start from the beginning
					num_of_aupdating_chars++;
					 }	
					
				}
			}
			
			if(index!=0) {//case that finish  the whole list and find the string all the way
				
				if(longest_bits_in_dictionary<Integer.toBinaryString(index).length())
					longest_bits_in_dictionary=Integer.toBinaryString(index).length();
				
					out_from_algo[index_of_out_from_algo]=Integer.toBinaryString(index);
					index_of_out_from_algo++;
				
			
				 dictionary_ascii[index_after_255]=str_combine_firstV_secondV;
				 index_after_255++;
				 count_the_time_of_pushing_to_dictionary++;
			}  
		}
		if(one_char) {//case that the file contain only one char extremely case
			
				out_from_algo[0]=Integer.toBinaryString(cur_ptr.data);
				this.final_num_of_chars=8;
				longest_bits_in_dictionary=8;
	
			
			
		}
		if(check_if_dictionary_change) {
			
				out_from_algo[index_of_out_from_algo]=Integer.toBinaryString(cur_ptr.data);
				index_of_out_from_algo++;		
		}
	System.out.println("writing to the compressed file the pre essential information(num_of_chars_in_the_file, etc)......");
	send_bit_by_bit_to_write(output, get_num_of_bits(num_of_chars_in_the_file),8);
	send_bit_by_bit_to_write(output, num_of_chars_in_the_file,get_num_of_bits(num_of_chars_in_the_file));
	send_bit_by_bit_to_write(output, longest_bits_in_dictionary,8);//passing the size of the array by passing information to the output file
	send_bit_by_bit_to_write(output, get_num_of_bits(num_of_aupdating_chars),8);
	send_bit_by_bit_to_write(output, num_of_aupdating_chars,get_num_of_bits(num_of_aupdating_chars));
	this.num_of_chars_in_org_file=num_of_chars_in_the_file;
	
		
	System.out.println("writing to the compressed file the all output......");	
		for(int i=0;i<num_of_aupdating_chars;i++) {
			if(out_from_algo[i]=="")
				break;
			send_bit_by_bit_to_write(output,Integer.parseInt(out_from_algo[i],2),longest_bits_in_dictionary);
		
		}
	
	output.write(Integer.parseInt(complete_8_by_adding_zero_at_right(this.write_to_file), 2));
	
	this.write_to_file="";
	

	input.close();
	output.close();
	double avg_str_long_repit=find_avg_long_repit(long_str_repit);
	this.avg_str_long=avg_str_long_repit;
	
	System.out.println("\nfinish Successfully LZW compressing!!");
}


public void Decompress(String[] input_names, String[] output_names) throws IOException{
	
	System.out.println("\nstart LZW decompressing......");
	FileInputStream input = null;
	FileOutputStream output=null;
	input = new FileInputStream(input_names[0]);
	output = new FileOutputStream(output_names[0]);
	
	System.out.println("\nreading the all essential information......");
	
	int num_of_bits_of_num_chars_in_orignal_file=Integer.parseInt(read_func(input,8), 2);	
	int num_of_chars_in_orignal_file=Integer.parseInt(read_func(input,num_of_bits_of_num_chars_in_orignal_file), 2);
	System.out.println("the number of chars in the original file is: "+num_of_chars_in_orignal_file);
	
	String longest= read_func(input,8);
	int longest_bits=Integer.parseInt(longest,2);
	System.out.println("the longest number of bits tn the file is: "+longest_bits);

	int amount_of_bits=Integer.parseInt(read_func(input,8), 2);	
	int array_size=Integer.parseInt(read_func(input,amount_of_bits), 2);

	
	String []dictionary_decompress=new String[256+num_of_chars_in_orignal_file];
	int index_dictionary_decompress=256;
	//Initialize the array with "" except the 256 first chars
	for(int i=256;i<256+num_of_chars_in_orignal_file;i++) {
		dictionary_decompress[i]="";
	}
	
	String []out_of_algo=new String[num_of_chars_in_orignal_file];
	for(int i=0;i<num_of_chars_in_orignal_file;i++) {
		out_of_algo[i]="";
	}
	int index_out_of_algo=0;
	
	int []arr_file_input=new int[array_size];
	System.out.println("reading the all compressed file......");
	for(int i=0;i<array_size;i++) {
		arr_file_input[i]=Integer.parseInt(read_func(input, longest_bits), 2);
	}
	
	System.out.println("creating the LZW decompress dictionary......");
	for(int i=0;i<256;i++) {
		dictionary_decompress[i]=String.valueOf((char)i);
	}
	

	
	int first=0;
	String first_str="";
	int second=0;
	String second_str="";
	String first_char_of_second="";
	for(int i=0;i<array_size-1;i++) {
		first=arr_file_input[i];
		first_str=dictionary_decompress[first];
		second=arr_file_input[i+1];
		if(i==0) {//extream csae that we have 256 and above on the second input
			if(second>255) {
				first_char_of_second=first_str;
				dictionary_decompress[second]=first_str;
			}
		}
		second_str=dictionary_decompress[second];
		if(i!=0 || second<256)
		   first_char_of_second=String.valueOf(dictionary_decompress[second].charAt(0));
		if(i==0) {
			dictionary_decompress[index_dictionary_decompress]=first_str+first_char_of_second;
			dictionary_decompress[index_dictionary_decompress+1]=second_str;
		}
		else {
			dictionary_decompress[index_dictionary_decompress]+=first_char_of_second;
			second_str=dictionary_decompress[second];
			dictionary_decompress[index_dictionary_decompress+1]=second_str;
		}
		out_of_algo[index_out_of_algo]=dictionary_decompress[first];
		index_out_of_algo++;
		index_dictionary_decompress++;
	}
	out_of_algo[index_out_of_algo]=dictionary_decompress[second];
	for(int i=0;i<index_out_of_algo+1;i++) {
	}	
	System.out.println("writing the decompress to the file......");
	char[] ch_of_out_of_algo=new char[num_of_chars_in_orignal_file];
	ch_of_out_of_algo=separate_arrSTR_to_arrCHAR(out_of_algo);
	for(int i=0;i<num_of_chars_in_orignal_file;i++) {
		output.write(ch_of_out_of_algo[i]);
	}
	input.close();
	output.close();
	
	System.out.println("\nfinish Successfully LZW decompressing......");
	System.out.println("\nworked Successfully!!!");
	
}
}


