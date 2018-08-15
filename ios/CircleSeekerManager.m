//
//  CircleSeekerManager.m
//  RNReactNativeCustomView
//
//  Created by Duy Dat Pham on 8/14/18.
//  Copyright © 2018 Facebook. All rights reserved.
//
#import "CircleSeekerManager.h"
#import "CircleSeeker.h"
#import "Donut.h"
#define UIColorFromRGB(rgbValue) \
[UIColor colorWithRed:((float)((rgbValue & 0xFF0000) >> 16))/255.0 \
green:((float)((rgbValue & 0x00FF00) >>  8))/255.0 \
blue:((float)((rgbValue & 0x0000FF) >>  0))/255.0 \
alpha:1.0]
@implementation CircleSeekerManager

RCT_EXPORT_MODULE();

-(UIColor *)colorFromHexString:(NSString *)hexString {
    unsigned rgbValue = 0;
    NSScanner *scanner = [NSScanner scannerWithString:hexString];
    [scanner setScanLocation:1]; // bypass '#' character
    [scanner scanHexInt:&rgbValue];
    return [UIColor colorWithRed:((rgbValue & 0xFF0000) >> 16)/255.0 green:((rgbValue & 0xFF00) >> 8)/255.0 blue:(rgbValue & 0xFF)/255.0 alpha:1.0];
}
- (UIView *)view
{
    CircleSeeker* circularSlider = [[CircleSeeker alloc] init];
    circularSlider.handleType = EFBigCircle;
    [circularSlider setBridge:self.bridge];
    return circularSlider;
//    Donut * donut = [[Donut alloc] init];// [Donut new];
//    donut.frame = CGRectMake(0, 0, 300, 300);
//    donut.baseColour = [[UIColor blackColor] colorWithAlphaComponent:0.2f];
//    donut.fromColour = [self colorFromHexString:@"#3081ED"];
//    donut.toColour = [self colorFromHexString:@"#51C3F1"];
//    donut.lineWidth = 20.0f;
//    donut.duration = 2.0f;
//    [donut layout];
//    [donut animateTo:0.6f];
//    return donut;
}

//RCT_EXPORT_VIEW_PROPERTY(valueEnd, NSNumber);
//RCT_EXPORT_VIEW_PROPERTY(withLine, NSNumber);
//RCT_EXPORT_VIEW_PROPERTY(withLineBackground, NSNumber);
//RCT_EXPORT_VIEW_PROPERTY(colorCircleBackground, UIColor);
//RCT_EXPORT_VIEW_PROPERTY(colorPointEnd, UIColor);


RCT_EXPORT_VIEW_PROPERTY(withLine, NSNumber);
RCT_EXPORT_VIEW_PROPERTY(value, NSNumber);
RCT_EXPORT_VIEW_PROPERTY(withLineBackground, NSNumber);
RCT_EXPORT_VIEW_PROPERTY(colorCircleBackground, UIColor);
RCT_EXPORT_VIEW_PROPERTY(colorPoint, UIColor);
RCT_EXPORT_VIEW_PROPERTY(colorCircle, UIColor);
RCT_EXPORT_VIEW_PROPERTY(enable, BOOL);

@end

