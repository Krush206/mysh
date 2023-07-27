import java.util.*;

public class Shell
{
  public static native int changeDirectory(String s);

  public static void main(String[] args)
  {
    Scanner input = new Scanner(System.in);
    String cmd;
    ArrayList<String> argv;

    System.loadLibrary("chdir");
    while(true)
    {
      System.out.print("> ");
      cmd = input.nextLine();

      if(cmd.isEmpty())
        continue;

      argv = parse(cmd);

      if(argv.isEmpty())
        continue;
      else if(shbins(argv))
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
          else if(shbins(argv))
          {
            argv.clear();
            break;
          }
          shexec(argv);
          argv.clear();
          break;
        case ' ':
          break;
        default:
          for(; i < s.length() && s.charAt(i) != ' '; i++)
          {
            sc = s.charAt(i);
            ps += sc.toString();
          }

          argv.add(ps);
          ps = new String();
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

  public static boolean shbins(ArrayList<String> bcmd)
  {
    String[] bcmds = { "exit", "chdir" };
    int i;

    for(i = 0; i < bcmds.length; i++)
      if(bcmds[i].compareTo(bcmd.get(0)) == 0)
      {
        switch(i)
        {
          case 0:
          {
            Runtime shexit = Runtime.getRuntime();

            shexit.exit(0);
          }
          case 1:
          if(bcmd.size() == 1)
          {
            changeDirectory(System.getenv("HOME"));

            return true;
          }

          {
            String dir = new String();
            int j;

            for(j = 0; j < bcmd.size() - 1; j++)
              if(j > 0)
                dir += " " + bcmd.get(j + 1);
              else
                dir = bcmd.get(j + 1);
            if(changeDirectory(dir) < 0)
              System.err.println(dir + ": No such directory.");
          }
        }

        return true;
      }

    return false;
  }
}
