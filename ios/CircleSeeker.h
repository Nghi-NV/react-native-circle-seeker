//
//  CircleSeeker
//  Awake
//
//  Created by Eliot Fowler on 12/3/13.
//  Copyright (c) 2013 Eliot Fowler. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <React/RCTBridgeModule.h>

@interface CircleSeeker : UIControl

typedef NS_ENUM(NSInteger, EFHandleType) {
    EFSemiTransparentWhiteCircle,
    EFSemiTransparentBlackCircle,
    EFDoubleCircleWithOpenCenter,
    EFDoubleCircleWithClosedCenter,
    EFBigCircle
};

@property (nonatomic) RCTBridge *bridge;

@property (nonatomic) float minimumValue;
@property (nonatomic) float maximumValue;
@property (nonatomic) float currentValue;

@property (nonatomic) BOOL enable;
@property (nonatomic) int lineWidth;
@property (nonatomic) int lineWidthBackground;
@property (nonatomic) int lineRadiusDisplacement;
@property (nonatomic, strong) UIColor* filledColor;
@property (nonatomic, strong) UIColor* colorCircleBackground;

@property (nonatomic, strong) UIColor* colorPoint;
@property (nonatomic) EFHandleType handleType;

@property (nonatomic, strong) UIFont* labelFont;
@property (nonatomic, strong) UIColor* labelColor;
@property (nonatomic, assign) NSInteger labelDisplacement;
@property (nonatomic) BOOL snapToLabels;




-(void)setInnerMarkingLabels:(NSArray*)labels;

@end
