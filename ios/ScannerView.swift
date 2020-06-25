//
//  ScannerView.swift
//  FirebaseIdScanner
//
//  Created by Jing Piao on 24/6/20.
//  Copyright © 2020 Facebook. All rights reserved.
//

import UIKit

class ScannerView: UIView {
    weak var scannerViewController: ScannerViewController?
    @objc var onSuccess: RCTDirectEventBlock?
    
    override init(frame: CGRect) {
        super.init(frame: frame)
    }
    required init?(coder aDecoder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    override func layoutSubviews() {
        super.layoutSubviews()
        
        if scannerViewController == nil {
            embed()
        } else {
            scannerViewController?.view.frame = bounds
        }
    }
    
    private func embed() {
        guard
            let parentVC = parentViewController
            else {
                return
        }
        
        
        let vc = ScannerViewController()
        vc.scannerView = self
        
        parentVC.addChild(vc)
        addSubview(vc.view)
        vc.view.frame = bounds
        vc.didMove(toParent: parentVC)
        self.scannerViewController = vc
    }
    
    func onHaveResult(_ result: String) {
        print("set onPress")
        let dict:[String:String] = [
            "result": result,
        ]
        onSuccess!(dict);
    }
}

extension UIView {
    var parentViewController: UIViewController? {
        var parentResponder: UIResponder? = self
        while parentResponder != nil {
            parentResponder = parentResponder!.next
            if let viewController = parentResponder as? UIViewController {
                return viewController
            }
        }
        return nil
    }
}
