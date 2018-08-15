//
//  Donut.h
//  RNReactNativeCustomView
//
//  Created by Duy Dat Pham on 8/15/18.
//  Copyright © 2018 Facebook. All rights reserved.
//
#import <UIKit/UIKit.h>

#define DEGREES_TO_RADIANS(x) (M_PI * (x) / 180.0)
@interface Donut : UIControl

@property (nonatomic) float minimumValue;
@property (nonatomic) float maximumValue;
@property (nonatomic) float currentValue;

    @property UIColor * fromColour;
    @property UIColor * toColour;
    @property UIColor * baseColour;
    @property float lineWidth;
@property float fromPercentage;
@property float duration;
    -(void)layout;
    -(void)animateTo:(float)percentage;
@end
