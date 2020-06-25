//
//  ScannerViewManager.swift
//  FirebaseIdScanner
//
//  Created by Jing tai Piao on 23/6/20.
//  Copyright © 2020 Facebook. All rights reserved.
//

@objc(ScannerViewManager)
class ScannerViewManager: RCTViewManager {
    override func view() -> UIView! {
        return ScannerView()
    }
}
