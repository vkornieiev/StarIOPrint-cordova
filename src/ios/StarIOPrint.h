//
//  StarIOPrint.h
//  CordovaStarIOPlugin
//
//  Created by Vladyslav on 8/23/16.
//
//

#import <Cordova/CDV.h>

@interface StarIOPrint : CDVPlugin

- (void)cordovaPOSPrint:(CDVInvokedUrlCommand *)command;
- (UIImage *)generateImageFromText:(NSString *)text;

@end
