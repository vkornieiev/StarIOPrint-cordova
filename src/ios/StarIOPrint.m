//
//  StarIOPrint.m
//  CordovaStarIOPlugin
//
//  Created by Vladyslav on 8/23/16.
//
//

#import "StarIOPrint.h"

#import "RasterDocument.h"
#import "StarBitmap.h"
#import <StarIO/SMPort.h>

#import <sys/time.h>

@implementation StarIOPrint

- (void)checkPrinterConnectionStatus:(CDVInvokedUrlCommand *)command {
    
    NSArray *printers = [SMPort searchPrinter];
    
    PortInfo *port = [printers firstObject];
    
    NSLog(@"%@",port.portName);
    
    SMPort *starPort = [SMPort getPort:port.portName :@"9100" :10000];
    
    BOOL success = [starPort getOnlineStatus];
    
    NSString *successString = success ? @"YES" : @"NO";
    
    [self.commandDelegate sendPluginResult:[CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:successString] callbackId:command.callbackId];
    
}

- (void)cordovaPOSPrint:(CDVInvokedUrlCommand *)command {
    
    UIImage *imageToPrint = [self generateImageFromText:[command.arguments firstObject]];
    
    //Then print Image
    
    [self print:imageToPrint];
    
    //Success
    
    BOOL success = true;
    
    NSString *successString = success ? @"YES" : @"NO";
    
    [self.commandDelegate sendPluginResult:[CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:successString] callbackId:command.callbackId];
    
}

- (UIImage *)generateImageFromText:(NSString *)text {
    
    UILabel *testLabel = [[UILabel alloc] init];
    testLabel.text = text;
    testLabel.font = [UIFont systemFontOfSize:50];
    testLabel.textColor = [UIColor blackColor
                           ];
    testLabel.backgroundColor = [UIColor whiteColor];
    [testLabel sizeToFit];
    
    UIGraphicsBeginImageContext(testLabel.bounds.size);
    [testLabel.layer renderInContext:UIGraphicsGetCurrentContext()];
    UIImage *viewImage = UIGraphicsGetImageFromCurrentImageContext();
    UIGraphicsEndImageContext();
    
    return viewImage;
}

- (void)print:(UIImage *)imgData {
    NSUserDefaults *pref=[NSUserDefaults standardUserDefaults];
    NSString *tempIPStr= @"10.3.1.172";
    
    if (tempIPStr && tempIPStr.length > 0){
        id portObj = [pref objectForKey:@"PrinterPort"];
        if (!portObj || [portObj integerValue] == 0){
            portObj = @"9100";
        }
        NSString *portString = [NSString stringWithFormat:@"%ld", (long)[portObj integerValue]];
        
        NSString *fullIP = @"TCP:";
        fullIP = [fullIP stringByAppendingString:tempIPStr];
        
        RasterDocument *rasterDoc = [[RasterDocument alloc] initWithDefaults:RasSpeed_Medium endOfPageBehaviour:RasPageEndMode_FeedAndFullCut endOfDocumentBahaviour:RasPageEndMode_FeedAndFullCut topMargin:RasTopMargin_Standard pageLength:0 leftMargin:0 rightMargin:0];
        
        StarBitmap *starbitmap = [[StarBitmap alloc] initWithUIImage:imgData :576 :false];
        
        NSMutableData *commandsToPrint = [[NSMutableData alloc] init];
        NSData *shortcommand = [rasterDoc BeginDocumentCommandData];
        [commandsToPrint appendData:shortcommand];
        
        shortcommand = [starbitmap getImageDataForPrinting:YES];
        [commandsToPrint appendData:shortcommand];
        
        shortcommand = [rasterDoc EndDocumentCommandData];
        [commandsToPrint appendData:shortcommand];
        
        [self sendCommand:commandsToPrint portName:fullIP portSettings:portString timeoutMillis:10000];
    }
}

- (void)sendCommand:(NSData *)commandsToPrint portName:(NSString *)portName portSettings:(NSString *)portSettings timeoutMillis:(u_int32_t)timeoutMillis
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
            [self showAlert:@"Fail to Open Port.\nRefer to \"getPort API\" in the manual."];
            return;
        }
        
        StarPrinterStatus_2 status;
        [starPort beginCheckedBlock:&status :2];
        if (status.offline == SM_TRUE) {
            [self showAlert:@"Printer is offline"];
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
            [self showAlert:@"Write port timed out"];
            return;
        }
        
        starPort.endCheckedBlockTimeoutMillis = 30000;
        [starPort endCheckedBlock:&status :2];
        if (status.offline == SM_TRUE) {
            [self showAlert:@"Printer is offline"];
            return;
        }
    }
    @catch (PortException *exception)
    {
        [self showAlert:@"Write port timed out"];
    }
    @finally
    {
        free(dataToSentToPrinter);
        [SMPort releasePort:starPort];
    }
}

- (void)showAlert:(NSString *)alertText {
    UIAlertController *alert = [UIAlertController alertControllerWithTitle:@"Error" message:alertText preferredStyle:UIAlertControllerStyleAlert];
    
    [alert addAction:[UIAlertAction actionWithTitle:@"Ok" style:UIAlertActionStyleCancel handler:nil]];
    
    //[self presentViewController:alert animated:YES completion:nil];
}

@end
