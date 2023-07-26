@interface Shell: NSObject
- (int) shbins: (NSArray *) bcmd;
- (void) shexec: (NSArray *) argv;
- (NSArray *) parse: (NSString *) s;
@end
