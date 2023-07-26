#import <Foundation/Foundation.h>
#import <unistd.h>
#import <sys/wait.h>
#import "Shell.h"

@implementation NSString (getchar)
- (NSString *) getCharFromStdin
{
  NSMutableString *str = [NSMutableString new];
  unichar c;

  while((c = getchar()) != '\n')
    [str appendFormat: @"%c", c];

  return str;
}
@end

@implementation Shell
- (int) shbins: (NSArray *) bcmd
{
  NSArray *bcmds = @[ @"exit", @"chdir" ];
  int i;

  for(i = 0; i < [bcmds count]; i++)
    if([[bcmd firstObject] isEqualToString: [bcmds objectAtIndex: i]])
    {
      switch(i)
      {
        case 0:
          exit(0);
        case 1:
        if([bcmd count] == 1)
        {
          chdir(getenv("HOME"));

          return 1;
        }

        {
          NSMutableString *bdir = [NSMutableString new];
          const char *cstr;
          int j;

          for(j = 0; j < [bcmd count] - 1; j++)
            if(j)
              [bdir appendFormat: @" %@", [bcmd objectAtIndex: j + 1]];
            else
              [bdir appendFormat: @"%@", [bcmd objectAtIndex: j + 1]];
          if(chdir(cstr = [bdir UTF8String]) < 0)
            fprintf(stderr, "%s: No such directory.\n", cstr);
        }
      }

      return 1;
    }

  return 0;
}

- (void) shexec: (NSArray *) argv
{
  const char *shcmd[[argv count] + 1];
  int i;

  for(i = 0; i < [argv count]; i++)
    shcmd[i] = [[argv objectAtIndex: i] UTF8String];
  shcmd[sizeof shcmd / sizeof *shcmd - 1] = NULL;

  if(!fork())
  {
    if(execvp(*shcmd, shcmd) < 0)
      fprintf(stderr, "%s: Command not found.\n", *shcmd);
    exit(0);
  }
  wait(NULL);
}

- (NSArray *) parse: (NSString *) s
{
  NSMutableString *ps = [NSMutableString new];
  NSMutableArray *argv = [NSMutableArray new];
  int i;

  for(i = 0; i < [s length]; i++)
    switch([s characterAtIndex: i])
    {
      case ' ':
        break;
      case ';':
        if(![argv count])
          break;
        else if([self shbins: argv])
        {
          [argv removeAllObjects];
          break;
        }
        [self shexec: argv];
        [argv removeAllObjects];
        break;
      default:
        for(; i < [s length] && [s characterAtIndex: i] != ' '; i++)
          [ps appendFormat: @"%c", [s characterAtIndex: i]];

        [argv addObject: ps];
        ps = [NSMutableString new];
    }

  return argv;
}
@end

int main(void)
{
  NSAutoreleasePool *pool = [NSAutoreleasePool new];
  Shell *sh = [Shell new];
  NSString *cmd = [NSString new];
  NSArray *argv;

  while(true)
  {
    printf("> ");
    cmd = [cmd getCharFromStdin];

    if(![cmd length])
      continue;

    argv = [sh parse: cmd];

    if(![argv count])
      continue;
    else if([sh shbins: argv])
      continue;

    [sh shexec: argv];
  }

  [pool drain];
}
