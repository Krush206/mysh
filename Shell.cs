namespace System;

public class Shell
{
  public static void Main(String[] args)
  {
    List<String> argv;
    String cmd;
    
    while(true)
    {
      Console.Write("> ");
      cmd = Console.ReadLine();
      
      argv = parse(cmd);

      if(argv.Count == 0)
        continue;

      shexec(argv);
    }
  }

  public static List<String> parse(String s)
  {
    String ps = new String("");
    List<String> argv = new List<String>();
    int i;

    for(i = 0; i < s.Length; i++)
      switch(s[i])
      {
        case ';':
          if(argv.Count == 0)
            break;
          shexec(argv);
          argv.Clear();
          break;
        case ' ':
          break;
        default:
          for(; i < s.Length && s[i] != ' '; i++)
            ps += s[i];

          argv.Add(ps);
          ps = new String("");
          break;
      }

    return argv;
  }

  public static void shexec(List<String> argv)
  {
    try
    {
      String cmd = argv[0];
      Diagnostics.Process shproc;

      argv.RemoveAt(0);
      shproc = Diagnostics.Process.Start(cmd, argv);
      shproc.WaitForExit();
    }
    catch(Exception e)
    {
      Console.Write(e.Message);
    }
  }
}
