package nomad.com.message;

public interface StringList {
	public long size();
	public boolean isEmpty();
	public void clear();
	public void add(String x);
	public String get(int i);
	public void removeItem(int i);
}
