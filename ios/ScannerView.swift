//
//  ScannerView.swift
//  FirebaseIdScanner
//
//  Created by Jing tai Piao on 23/6/20.
//  Copyright © 2020 Facebook. All rights reserved.
//

import UIKit

class ScannerView: UIView {
    @objc var count = 0 {
        didSet {
//            button.setTitle(String(describing: count), for: .normal)
        }
    }
    override init(frame: CGRect) {
        super.init(frame: frame)
        self.addSubview(button)
//        increment()
    }
    required init?(coder aDecoder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    lazy var button: UIButton = {
//        let b = UIButton.init(type: UIButton.ButtonType.custom)
        let b = UIButton(type: UIButton.ButtonType.custom) as UIButton
        b.setImage(UIImage(named: "camera"), for: .normal)
        b.frame = CGRect(x: 10, y: 100, width: 100, height: 100)
//        b.setTitle("testing", for: .normal)
//        b.translatesAutoresizingMaskIntoConstraints = false
        
//        b.frame = .zero
        b.tintColor = .white
        b.backgroundColor = .orange
//        b.titleLabel?.font = UIFont.systemFont(ofSize: 50)
//        b.autoresizingMask = [.flexibleWidth, .flexibleHeight]
        b.addTarget(
            self,
            action: #selector(increment),
            for: .touchUpInside
        )
        return b
    }()
    @objc func increment() {
        count += 1
    }
}
