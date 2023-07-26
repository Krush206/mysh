import java.util.*;

public class Shell
{
  public static void main(String[] args)
  {
    Scanner input = new Scanner(System.in);
    String cmd;
    ArrayList<String> argv;

    while(true)
    {
      System.out.print("> ");
      cmd = input.nextLine();

      if(cmd.isEmpty())
        continue;

      argv = parse(cmd);

      if(argv.isEmpty())
        continue;

      shexec(argv);
    }
  }

  public static ArrayList<String> parse(String s)
  {
    Character sc;
    String ps = new String();
    ArrayList<String> argv = new ArrayList<String>();
    int i;

    for(i = 0; i < s.length(); i++)
      switch(s.charAt(i))
      {
        case ';':
          if(argv.isEmpty())
            break;
          shexec(argv);
          argv.clear();
          break;
        case ' ':
          break;
        default:
          sc = s.charAt(i);
          ps = sc.toString();
          for(i++; i < s.length() && s.charAt(i) != ' '; i++)
          {
            sc = s.charAt(i);
            ps = ps.concat(sc.toString());
          }
  
          argv.add(ps);
      }

    return argv;
  }

  public static void shexec(ArrayList<String> argv)
  {
    try
    {
      String[] cmd_arr = new String[argv.size()];
      Runtime shcmd = Runtime.getRuntime();
      Process shproc = shcmd.exec(argv.toArray(cmd_arr));
      int pchar;

      while((pchar = shproc.getInputStream().read()) > 0)
        System.out.print((char) pchar);
    }
    catch(Exception e)
    {
      System.out.println(e.getMessage());
    }
  }
}
