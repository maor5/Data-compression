package Huffman_Algo;


public class list {
	
		public list_node head; 
		public list_node tail;


		public list() {
		this.head = null;
		this.tail=null;
		}
		public boolean isEmpty()  {
		return this.head == null;
		} 

		public void insert_from_back( int ch) {
		list_node node_to_insert = new list_node(ch,null); // creating new node
		if(this.head==null && this.tail==null ) {//no nodes
			this.head=node_to_insert;
			this.tail=node_to_insert;
		}

			else {
				list_node tmp_tail=this.tail;
				this.tail=node_to_insert;
				tmp_tail.next=node_to_insert;
			}
					
			}

}
