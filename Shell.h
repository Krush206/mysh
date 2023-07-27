@interface Shell: NSObject
- (BOOL) shbins: (NSArray *) bcmd;
- (void) shexec: (NSArray *) argv;
- (NSArray *) parse: (NSString *) s;
@end
