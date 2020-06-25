//
//  ScaledElementProcessor.swift
//  FirebaseIdScanner
//
//  Created by Jing tai Piao on 23/6/20.
//  Copyright © 2020 Facebook. All rights reserved.
//

import Foundation

#if canImport(FirebaseMLVision)
import FirebaseMLVision

struct ScaledElement {
    let frame: CGRect
    let shapeLayer: CALayer
}


class ScaledElementProcessor {
    let vision = Vision.vision()
    var textRecognizer: VisionTextRecognizer!
    
    init() {
        textRecognizer = vision.onDeviceTextRecognizer()
    }
    
    func process(
        in imageView: UIImageView,
        callback: @escaping (_ text: String, _ scaledElements: [ScaledElement]) -> Void
    ) {
        guard let image = imageView.image else { return }
        let visionImage = VisionImage(image: image)
        
        textRecognizer.process(visionImage) { result, error in
            guard
                error == nil,
                let result = result,
                !result.text.isEmpty
                else {
                    callback("", [])
                    return
            }
            
            var scaledElements: [ScaledElement] = []
            var likelyLicenseNo: String = "";
            for block in result.blocks {
                for line in block.lines {
                    for element in line.elements {
                        // detect if it is possible a license number
                        let elementText = element.text
                        var isLikelyLicenseNo = false;
                        if (self.validate(elementText)) {
                            isLikelyLicenseNo = true;
                            
                            // remove all spaces
                            likelyLicenseNo = elementText.filter { !$0.isWhitespace }
                        }
                        
                        let frame = self.createScaledFrame(
                            featureFrame: element.frame,
                            imageSize: image.size,
                            viewFrame: imageView.frame)
                        
                        let shapeLayer = self.createShapeLayer(frame: frame, isLikelyLicenseNo: isLikelyLicenseNo)
                        let scaledElement = ScaledElement(frame: frame, shapeLayer: shapeLayer)
                        scaledElements.append(scaledElement)
                    }
                    
                }
            }
            
            callback(likelyLicenseNo, scaledElements)
        }
    }
    
    // MARK: - private
    private func validate(_ str: String) -> Bool {
        guard let regex = try? NSRegularExpression(pattern: "^(\\s*\\d\\s*){8,9}$") else { return false }
        let range = NSRange(location: 0, length: str.utf16.count)
        return regex.firstMatch(in: str, options: [], range: range) != nil
    }
    
    // MARK: - private
    private func createShapeLayer(frame: CGRect, isLikelyLicenseNo: Bool) -> CAShapeLayer {
        let bpath = UIBezierPath(rect: frame)
        let shapeLayer = CAShapeLayer()
        shapeLayer.path = bpath.cgPath
        
        if (isLikelyLicenseNo) {
             shapeLayer.strokeColor = Constants.licenseNoColor
        } else {
            shapeLayer.strokeColor = Constants.lineColor
        }

        shapeLayer.fillColor = Constants.fillColor
        shapeLayer.lineWidth = Constants.lineWidth
        return shapeLayer
    }
    
    // MARK: - private
    private enum Constants {
        static let lineWidth: CGFloat = 3.0
        static let lineColor = UIColor.yellow.cgColor
        static let fillColor = UIColor.clear.cgColor
        static let licenseNoColor = UIColor.orange.cgColor
    }
    
    // MARK: - private
    private func createScaledFrame(
        featureFrame: CGRect,
        imageSize: CGSize, viewFrame: CGRect)
        -> CGRect {
            let viewSize = viewFrame.size
            
            let resolutionView = viewSize.width / viewSize.height
            let resolutionImage = imageSize.width / imageSize.height
            
            var scale: CGFloat
            if resolutionView > resolutionImage {
                scale = viewSize.height / imageSize.height
            } else {
                scale = viewSize.width / imageSize.width
            }
            
            let featureWidthScaled = featureFrame.size.width * scale
            let featureHeightScaled = featureFrame.size.height * scale
            
            let imageWidthScaled = imageSize.width * scale
            let imageHeightScaled = imageSize.height * scale
            let imagePointXScaled = (viewSize.width - imageWidthScaled) / 2
            let imagePointYScaled = (viewSize.height - imageHeightScaled) / 2
            
            let featurePointXScaled = imagePointXScaled + featureFrame.origin.x * scale
            let featurePointYScaled = imagePointYScaled + featureFrame.origin.y * scale
            
            return CGRect(x: featurePointXScaled,
                          y: featurePointYScaled,
                          width: featureWidthScaled,
                          height: featureHeightScaled)
    }
}
#endif
