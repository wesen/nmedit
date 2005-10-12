package nomad.misc;

public class MalformedPropertyfileException extends Exception
{

  public MalformedPropertyfileException()
  { super(); }

  public MalformedPropertyfileException(String message)
  { super(message); }

  public MalformedPropertyfileException(String message, Throwable cause)
  { super(message, cause); }

  public MalformedPropertyfileException(Throwable cause)
  { super(cause); }

}