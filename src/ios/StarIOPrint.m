//
//  StarIOPrint.m
//  CordovaStarIOPlugin
//
//  Created by Vladyslav on 8/23/16.
//
//

#import "StarIOPrint.h"

#import "NSString+HTMLAttributedString.h"

#import "RasterDocument.h"
#import "StarBitmap.h"
#import <StarIO/SMPort.h>

#import <sys/time.h>

@implementation StarIOPrint {
    NSArray *_networkPrintersArray;
    PortInfo *_activePrinter;
}

#pragma mark - Plugin methods

- (void)getAvailablePrintersList:(CDVInvokedUrlCommand *)command {
    _networkPrintersArray = [SMPort searchPrinter];
    
    NSArray *availablePrintersDictionaryArray = [self convertPrintersObjectsArrayToDictionaryArray];
    
    if (_networkPrintersArray && [_networkPrintersArray count] > 0) {
        [self.commandDelegate runInBackground:^{
            [self.commandDelegate sendPluginResult:[CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsArray:availablePrintersDictionaryArray] callbackId:command.callbackId];
        }];
    } else {
        [self.commandDelegate runInBackground:^{
            [self.commandDelegate sendPluginResult:[CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsArray:availablePrintersDictionaryArray] callbackId:command.callbackId];
        }];
    }
}

- (void)connectToPrinter:(CDVInvokedUrlCommand *)command {
    
    NSString *portName = [command.arguments firstObject];
    
    if (_networkPrintersArray && [_networkPrintersArray count] > 0) {
        for (PortInfo *printer in _networkPrintersArray) {
            if ([printer.portName isEqualToString:portName]) {
                _activePrinter = printer;
                break;
            }
        }
    }
    
    NSLog(@"%@",_activePrinter.portName);
    
    if (_activePrinter) {
        [self.commandDelegate runInBackground:^{
            NSString *printerIP = [NSString stringWithFormat:@"Printer connected successfully! IP: %@", _activePrinter.portName];
            [self.commandDelegate sendPluginResult:[CDVPluginResult resultWithStatus:
                                                    CDVCommandStatus_OK messageAsString:printerIP] callbackId:command.callbackId];
        }];
    } else {
        [self.commandDelegate runInBackground:^{
            NSString *printerIP = [NSString stringWithFormat:@"Cannot connect to printer with IP: %@", _activePrinter.portName];
            [self.commandDelegate sendPluginResult:[CDVPluginResult resultWithStatus:
                                                    CDVCommandStatus_ERROR messageAsString:printerIP] callbackId:command.callbackId];
        }];
    }
}

- (void)cordovaPOSPrintText:(CDVInvokedUrlCommand *)command {
    
    NSString *text = [command.arguments firstObject];
    
    UILabel *label = [[UILabel alloc] init];
    label.numberOfLines = 0;
    label.text = text;
    label.font = [UIFont systemFontOfSize:50];
    label.backgroundColor = [UIColor whiteColor];
    label.textColor = [UIColor blackColor];
    [label sizeToFit];
    
    UIImage *imageToPrint = [self imageFromLabel:label];
    
    //Then print Image
    
    [self print:imageToPrint completion:^(NSError *error) {
        if (error == nil) {
            [self.commandDelegate runInBackground:^{
                [self.commandDelegate sendPluginResult:[CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:@"Printed Successfully!"] callbackId:command.callbackId];
            }];
        } else {
            [self.commandDelegate runInBackground:^{
                [self.commandDelegate sendPluginResult:[CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:error.localizedDescription] callbackId:command.callbackId];
            }];
        }
    }];
}

- (void)cordovaPOSPrintHTMLText:(CDVInvokedUrlCommand *)command {
    
    NSString *text = [command.arguments firstObject];
    [text attributedStringFromHTMLStringWithCompletion:^(NSAttributedString *attributedText) {
        UILabel *label = [UILabel new];
        label.numberOfLines = 0;
        label.backgroundColor = [UIColor whiteColor];
        label.attributedText = attributedText;
        [label sizeToFit];
        
        UIImage *imageToPrint = [self imageFromLabel:label];
        
        //Then print Image
        
        [self print:imageToPrint completion:^(NSError *error) {
            if (error == nil) {
                [self.commandDelegate runInBackground:^{
                    [self.commandDelegate sendPluginResult:[CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:@"Printed Successfully!"] callbackId:command.callbackId];
                }];
            } else {
                [self.commandDelegate runInBackground:^{
                    [self.commandDelegate sendPluginResult:[CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:error.localizedDescription] callbackId:command.callbackId];
                }];
            }
        }];
    }];
}

#pragma mark - Printer Logic

- (UIImage *)imageFromLabel:(UILabel)label {
    
    UIGraphicsBeginImageContext(label.bounds.size);
    [label.layer renderInContext:UIGraphicsGetCurrentContext()];
    UIImage *viewImage = UIGraphicsGetImageFromCurrentImageContext();
    UIGraphicsEndImageContext();
    
    return viewImage;
}

- (void)print:(UIImage *)imgData completion:(void(^)(NSError *error))completion {
    if (_activePrinter) {
        RasterDocument *rasterDoc = [[RasterDocument alloc] initWithDefaults:RasSpeed_Medium endOfPageBehaviour:RasPageEndMode_FeedAndFullCut endOfDocumentBahaviour:RasPageEndMode_FeedAndFullCut topMargin:RasTopMargin_Standard pageLength:0 leftMargin:0 rightMargin:0];
        
        StarBitmap *starbitmap = [[StarBitmap alloc] initWithUIImage:imgData :576 :false];
        
        NSMutableData *commandsToPrint = [[NSMutableData alloc] init];
        NSData *shortcommand = [rasterDoc BeginDocumentCommandData];
        [commandsToPrint appendData:shortcommand];
        
        shortcommand = [starbitmap getImageDataForPrinting:YES];
        [commandsToPrint appendData:shortcommand];
        
        shortcommand = [rasterDoc EndDocumentCommandData];
        [commandsToPrint appendData:shortcommand];
        
        [self sendCommand:commandsToPrint portName:_activePrinter.portName portSettings:"9100" timeoutMillis:10000 completion:^(NSError *error) {
            completion(error);
        }];
    }
}

- (void)sendCommand:(NSData *)commandsToPrint portName:(NSString *)portName portSettings:(NSString *)portSettings timeoutMillis:(u_int32_t)timeoutMillis completion:(void(^)(NSError *error))completion
{
    int commandSize = (int)commandsToPrint.length;
    unsigned char *dataToSentToPrinter = (unsigned char *)malloc(commandSize);
    [commandsToPrint getBytes:dataToSentToPrinter length:commandSize];
    
    SMPort *starPort = nil;
    @try
    {
        starPort = [SMPort getPort:portName :portSettings :timeoutMillis];
        
        //BOOL online = [starPort getOnlineStatus];
        if (starPort == nil)
        {
            NSError *error = [self generateErrorWithMessage:@"Fail to Open Port.\nRefer to \"getPort API\" in the manual." andCode:1001];
            completion(error);
            return;
        }
        
        StarPrinterStatus_2 status;
        [starPort beginCheckedBlock:&status :2];
        if (status.offline == SM_TRUE) {
            NSError *error = [self generateErrorWithMessage:@"Printer is offline" andCode:1002];
            completion(error);
            return;
        }
        
        struct timeval endTime;
        gettimeofday(&endTime, NULL);
        endTime.tv_sec += 30;
        
        int totalAmountWritten = 0;
        while (totalAmountWritten < commandSize)
        {
            int remaining = commandSize - totalAmountWritten;
            int amountWritten = [starPort writePort:dataToSentToPrinter :totalAmountWritten :remaining];
            totalAmountWritten += amountWritten;
            
            struct timeval now;
            gettimeofday(&now, NULL);
            if (now.tv_sec > endTime.tv_sec)
            {
                break;
            }
        }
        
        if (totalAmountWritten < commandSize)
        {
            NSError *error = [self generateErrorWithMessage:@"Write port timed out" andCode: 1003];
            completion(error);
            return;
        }
        
        starPort.endCheckedBlockTimeoutMillis = 30000;
        [starPort endCheckedBlock:&status :2];
        if (status.offline == SM_TRUE) {
            NSError *error = [self generateErrorWithMessage:@"Printer is offline" andCode:1004];
            completion(error);
            return;
        }
    }
    @catch (PortException *exception)
    {
        NSError *error = [self generateErrorWithMessage:@"Write port timed out" andCode:1005];
        completion(error);
    }
    @finally
    {
        free(dataToSentToPrinter);
        [SMPort releasePort:starPort];
        
        completion(nil);
    }
}

#pragma mark - Private

- (NSArray *)convertPrintersObjectsArrayToDictionaryArray {
    NSMutableArray *dictionaryArray = [[NSMutableArray alloc] init];
    
    for (PortInfo *printer in _networkPrintersArray) {
        NSDictionary *printerDictionary = @{
                                            @"portName":printer.portName,
                                            @"macAddress":printer.macAddress,
                                            @"modelName":printer.modelName
                                            };
        [dictionaryArray addObject:printerDictionary];
    }
    
    return [dictionaryArray copy];
}

- (NSError *)generateErrorWithMessage:(NSString *)messageText andCode:(NSInteger)code {
    NSDictionary *userInfo = @{
                               NSLocalizedDescriptionKey: NSLocalizedString(messageText, nil)
                               };
    NSError *error = [NSError errorWithDomain:@"com.StarIOPrint"
                                         code:code
                                     userInfo:userInfo];
    return error;
}

@end
