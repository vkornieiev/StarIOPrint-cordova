//
//  NSString+HTMLAttributedString.h
//  PrinterTest
//
//  Created by Vladyslav Korneyev on 12/6/17.
//  Copyright © 2017 Vladyslav. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface NSString (HTMLAttributedString)

- (void)attributedStringFromHTMLStringWithCompletion:(void(^)(NSAttributedString *))completion;

@end
