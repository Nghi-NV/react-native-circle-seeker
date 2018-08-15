//
//  UIView+Donut.h
//  RNReactNativeCustomView
//
//  Created by Duy Dat Pham on 8/15/18.
//  Copyright © 2018 Facebook. All rights reserved.
//

#import "Donut.h"

#define kDefaultFontSize 14.0f;
#define ToRad(deg)         ( (M_PI * (deg)) / 180.0 )
#define ToDeg(rad)        ( (180.0 * (rad)) / M_PI )
#define SQR(x)            ( (x) * (x) )


@implementation Donut {
    CAShapeLayer* mask;
    UIView* rotateView;
    UIView * dotView;
    int angle;
    int fixedAngle;
}

- (id)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
    if (self) {
        [self defaults];
        
        [self setFrame:frame];
    }
    return self;
}

- (id)initWithCoder:(NSCoder *)aDecoder {
    if ((self=[super initWithCoder:aDecoder])){
        [self defaults];
    }
    
    return self;
}


- (void)setFrame:(CGRect)frame {
    [super setFrame:frame];
    
    angle = [self angleFromValue];
}

- (void)defaults {
    // Defaults
    _fromPercentage = 0.1f;
    _maximumValue = 100.0f;
    _minimumValue = 0.0f;
    _currentValue = 0.0f;
}
-(void)layout {
    
    //vars
    float dimension = self.frame.size.width;
    
    //1. layout views
    
    //1.1 layout base track
    UIBezierPath * donut = [UIBezierPath bezierPathWithOvalInRect:CGRectMake(_lineWidth/2, _lineWidth/2, dimension-_lineWidth, dimension-_lineWidth)];
    CAShapeLayer * baseTrack = [CAShapeLayer layer];
    baseTrack.path = donut.CGPath;
    baseTrack.lineWidth = _lineWidth;
    baseTrack.fillColor = [UIColor clearColor].CGColor;
    baseTrack.strokeStart = 0.0f;
    baseTrack.strokeEnd = 1.0f;
    baseTrack.strokeColor = _baseColour.CGColor;
    baseTrack.lineCap = kCALineCapButt;
    [self.layer addSublayer:baseTrack];
    
    //1.2 clipView has mask applied to it
    UIView * clipView = [UIView new];
    clipView.frame =  self.bounds;
    [clipView setUserInteractionEnabled:NO];
    [self addSubview:clipView];
    
    //1.3 rotateView transforms with strokeEnd
    rotateView = [UIView new];
    rotateView.frame = self.bounds;
    [rotateView setUserInteractionEnabled:NO];
    [clipView addSubview:rotateView];
    
    //1.4 radialGradient holds an image of the colours
    UIImageView * radialGradient = [UIImageView new];
    radialGradient.frame = self.bounds;
    [radialGradient setUserInteractionEnabled:NO];
    [rotateView addSubview:radialGradient];
    
    
    
    //2. create colours fromColour --> toColour and add to an array
    
    //2.1 holds all colours between fromColour and toColour
    NSMutableArray * spectrumColours = [NSMutableArray new];
    
    //2.2 get RGB values for both colours
    double fR, fG, fB; //fromRed, fromGreen etc
    double tR, tG, tB; //toRed, toGreen etc
    [_fromColour getRed:&fR green:&fG blue:&fB alpha:nil];
    [_toColour getRed:&tR green:&tG blue:&tB alpha:nil];
    
    //2.3 determine increment between fromRed and toRed etc.
    int numberOfColours = 360;
    double dR = (tR-fR)/(numberOfColours-1);
    double dG = (tG-fG)/(numberOfColours-1);
    double dB = (tB-fB)/(numberOfColours-1);
    
    //2.4 loop through adding incrementally different colours
    //this is a gradient fromColour --> toColour
    for (int n = 0; n < numberOfColours; n++){
        [spectrumColours addObject:[UIColor colorWithRed:(fR+n*dR) green:(fG+n*dG) blue:(fB+n*dB) alpha:1.0f]];
    }
    
    
    //3. create a radial image using the spectrum colours
    //go through adding the next colour at an increasing angle
    
    //3.1 setup
    float radius = MIN(dimension, dimension)/2;
    float angle = 2 * M_PI/numberOfColours;
    UIBezierPath * bezierPath;
    CGPoint center = CGPointMake(dimension/2, dimension/2);
    
    UIGraphicsBeginImageContextWithOptions(CGSizeMake(dimension, dimension), true, 0.0);
    UIRectFill(CGRectMake(0, 0, dimension, dimension));
    
    //3.2 loop through pulling the colour and adding
    for (int n = 0; n<numberOfColours; n++){
        
        UIColor * colour = spectrumColours[n]; //colour for increment
        
        bezierPath = [UIBezierPath bezierPathWithArcCenter:center radius:radius startAngle:n * angle endAngle:(n + 1) * angle clockwise:YES];
        [bezierPath addLineToPoint:center];
        [bezierPath closePath];
        
        [colour setFill];
        [colour setStroke];
        [bezierPath fill];
        [bezierPath stroke];
    }
    
    //3.3 create image, add to the radialGradient and end
    [radialGradient setImage:UIGraphicsGetImageFromCurrentImageContext()];
    UIGraphicsEndImageContext();
    
    
    
    //4. create a dot to add to the rotating view
    //this covers the connecting line between the two colours
    
    //4.1 set up vars
    float containsDots = (M_PI * dimension) /*circumference*/ / _lineWidth; //number of dots in circumference
    float colourIndex = roundf((numberOfColours / containsDots) * (containsDots-0.5f)); //the nearest colour for the dot
    UIColor * closestColour = spectrumColours[(int)colourIndex]; //the closest colour

    //4.2 create dot
    dotView = [UIView new];
    dotView.frame =  self.bounds;
    [dotView setUserInteractionEnabled:NO];
    [self addSubview:dotView];
    
    UIImageView * dot = [UIImageView new];
    dot.frame = CGRectMake(dimension-_lineWidth, (dimension-_lineWidth)/2, _lineWidth, _lineWidth);
    dot.layer.cornerRadius = _lineWidth/2;
    dot.backgroundColor = [UIColor redColor];
    [dot setUserInteractionEnabled:NO];
    [dotView addSubview:dot];
    
    dotView.transform = CGAffineTransformMakeRotation(DEGREES_TO_RADIANS(360 * 0.5f - 90));
    rotateView.transform = CGAffineTransformMakeRotation(DEGREES_TO_RADIANS(360 * 0.5f));
    
    
    //5. create the mask
    mask = [CAShapeLayer layer];
    mask.path = donut.CGPath;
    mask.lineWidth = _lineWidth;
    mask.fillColor = [UIColor clearColor].CGColor;
    mask.strokeStart = 0;
    mask.strokeEnd = 0.5f;
    mask.strokeColor = [UIColor blackColor].CGColor;
    mask.lineCap = kCALineCapButt;
    
    //5.1 apply the mask and rotate all by -90 (to move to the 12 position)
    clipView.layer.mask = mask;
    clipView.transform = CGAffineTransformMakeRotation(DEGREES_TO_RADIANS(-90.0f));
    
}

- (void)setValueEnd:(NSNumber *)valueEnd
{
    float angle = [valueEnd floatValue];
    dotView.transform = CGAffineTransformMakeRotation(DEGREES_TO_RADIANS(angle - 90));
    rotateView.transform = CGAffineTransformMakeRotation(DEGREES_TO_RADIANS(angle));


    mask.strokeEnd = angle / 360;
}

-(void)animateTo:(float)percentage {
    
    float difference = fabsf(_fromPercentage - percentage);
    float fixedDuration = difference * _duration;
    
    //1. animate stroke End
    CABasicAnimation * strokeEndAnimation = [CABasicAnimation animationWithKeyPath:@"strokeEnd"];
    strokeEndAnimation.duration = fixedDuration;
    strokeEndAnimation.fromValue = @(_fromPercentage);
    strokeEndAnimation.toValue = @(percentage);
    strokeEndAnimation.fillMode = kCAFillModeForwards;
    strokeEndAnimation.removedOnCompletion = false;
    strokeEndAnimation.timingFunction = [CAMediaTimingFunction functionWithName:kCAMediaTimingFunctionLinear];
    [mask addAnimation:strokeEndAnimation forKey:@"strokeEndAnimation"];
    
    //2. animate rotation of rotateView
    CABasicAnimation * viewRotationAnimation = [CABasicAnimation animationWithKeyPath:@"transform.rotation.z"];
    viewRotationAnimation.duration = fixedDuration;
    viewRotationAnimation.fromValue = @(DEGREES_TO_RADIANS(360 * _fromPercentage));
    viewRotationAnimation.toValue = @(DEGREES_TO_RADIANS(360 * percentage));
    viewRotationAnimation.fillMode = kCAFillModeForwards;
    viewRotationAnimation.removedOnCompletion = false;
    viewRotationAnimation.timingFunction = [CAMediaTimingFunction functionWithName:kCAMediaTimingFunctionLinear];
    [rotateView.layer addAnimation:viewRotationAnimation forKey:@"viewRotationAnimation"];
    
    //3. update from percentage
    _fromPercentage = percentage;
    
}


#pragma mark - UIControl functions

-(BOOL) beginTrackingWithTouch:(UITouch *)touch withEvent:(UIEvent *)event {
    [super beginTrackingWithTouch:touch withEvent:event];
    NSLog(@"beginTrackingWithTouch ");
    return YES;
}

-(BOOL) continueTrackingWithTouch:(UITouch *)touch withEvent:(UIEvent *)event {
    [super continueTrackingWithTouch:touch withEvent:event];
    
    CGPoint lastPoint = [touch locationInView:self];
    [self moveHandle:lastPoint];
    [self sendActionsForControlEvents:UIControlEventValueChanged];
    
    return YES;
}

-(void)endTrackingWithTouch:(UITouch *)touch withEvent:(UIEvent *)event{
    [super endTrackingWithTouch:touch withEvent:event];
}


-(void)moveHandle:(CGPoint)point {
    CGPoint centerPoint;
    centerPoint = [self centerPoint];
    int currentAngle = floor(AngleFromNorth(centerPoint, point, NO));
    angle = currentAngle;
    _currentValue = [self valueFromAngle];
    
    [self setValueEnd: @(_currentValue)];
    [self setNeedsDisplay];
    NSLog(@"continueTrackingWithTouch : %f", _currentValue );
}
- (CGPoint)centerPoint {
    return CGPointMake(self.frame.size.width/2, self.frame.size.height/2);
}


static inline float AngleFromNorth(CGPoint p1, CGPoint p2, BOOL flipped) {
    CGPoint v = CGPointMake(p2.x-p1.x,p2.y-p1.y);
    float vmag = sqrt(SQR(v.x) + SQR(v.y)), result = 0;
    v.x /= vmag;
    v.y /= vmag;
    double radians = atan2(v.y,v.x);
    result = ToDeg(radians);
    return (result >=0  ? result : result + 360.0);
}

-(float) valueFromAngle {
    if(angle < 0) {
        _currentValue = -angle;
    } else {
        _currentValue = 270 - angle + 90;
    }
    fixedAngle = _currentValue;
    return (_currentValue*(_maximumValue - _minimumValue))/360.0f;
}

- (float)angleFromValue {
    angle = 360 - (360.0f*_currentValue/_maximumValue);
    
    if(angle==360) angle=0;
    
    return angle;
}
@end
