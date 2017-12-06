//
//  NSString+HTMLAttributedString.m
//  PrinterTest
//
//  Created by Vladyslav Korneyev on 12/6/17.
//  Copyright © 2017 Vladyslav. All rights reserved.
//

#import "NSString+HTMLAttributedString.h"

@implementation NSString (HTMLAttributedString)

- (void)attributedStringFromHTMLStringWithCompletion:(void(^)(NSAttributedString *))completion {
    NSData *data = [self dataUsingEncoding:NSUTF8StringEncoding];
    NSDictionary *options = @{
                              NSDocumentTypeDocumentAttribute: NSHTMLTextDocumentType,
                              NSCharacterEncodingDocumentAttribute: [NSNumber numberWithInt:NSUTF8StringEncoding]
                              };
    
    dispatch_async(dispatch_get_main_queue(), ^{
        NSAttributedString *attributedString = [[NSAttributedString alloc] initWithData:data options:options documentAttributes:nil error:nil];
        completion(attributedString);
    });
}

@end
