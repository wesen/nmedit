package org.nomad.util.debug;

public interface CascadeModel {

	public String prefix();
	
	public abstract class Cascadable implements CascadeModel {
		
		protected CascadeModel model = null;
		
		public Cascadable() {
			this(null);
		}
		
		public Cascadable(CascadeModel model) {
			this.model = model;
		}

		public String prefix() {
			return model==null ? "" : model.prefix();
		}
	}

	public class Constant extends Cascadable {
		private String prefix = "";
		
		public Constant (String prefix) {
			this(null, prefix);
		}
		
		public Constant (CascadeModel model, String prefix) {
			super(model);
			setPrefix(prefix);
		}
		
		public void setPrefix(String prefix) {
			this.prefix = prefix==null?"":prefix;
		}
		
		public String prefix() {
			return super.prefix()+prefix;
		}	
	}
	
	public class NumberPrefix extends Cascadable {

		private int number = 1;
		
		public NumberPrefix() {
			super();
		}
		
		public NumberPrefix(CascadeModel model) {
			super(model);
		}
		
		public NumberPrefix(CascadeModel model, int number) {
			super(model);
			this.number = number;
		}
		
		public int increment() {
			return ++number;
		}
		
		public void setNumber(int number) {
			this.number = number;
		}
		
		public int getNumber() {
			return number;
		}

		public String prefix() {
			return prefix(getNumber());
		}

		public String prefix(int number) {
			return super.prefix()+" "+number+":";
		}	
		
	}
	
	public class LineNumberPrefix extends NumberPrefix {

		public LineNumberPrefix() {
			super();
		}
		
		public LineNumberPrefix(CascadeModel model) {
			super(model);
		}
		
		public LineNumberPrefix(CascadeModel model, int number) {
			super(model, number);
		}

		public String prefix() {
			return prefix(increment());
		}	
		
	}
	
}
