import UIKit
import AVFoundation
import MobileCoreServices

class ScannerViewController: UIViewController {
    var imageView: UIImageView = UIImageView.init()
    var textView: UITextView = UITextView.init()
    var button: UIButton!
    var screenWidth: CGFloat = UIScreen.main.bounds.width
    
    let processor = ScaledElementProcessor()
    var frameSublayer = CALayer()
    var scannedText: String = "Detected text can be edited here." {
        didSet {
            textView.text = scannedText
        }
    }
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        button = UIButton(frame: CGRect(x: 100, y: 440, width: 100, height: 50))
        button.backgroundColor = .orange
        button.setTitle("Test Button!", for: .normal)
        button.addTarget(self, action: #selector(buttonAction), for: .touchUpInside)
        
        self.view.addSubview(button)
        addImageView()
        addTextView()
        
        //        imageView.layer.addSublayer(frameSublayer)
        drawFeatures(in: imageView)
    }
    
    func addImageView(name:String = "default") {
        let image = UIImage(named: name)
        imageView.image = image
        imageView.frame = CGRect(x: 40, y: 40, width: screenWidth - 80, height: 240)
        view.addSubview(imageView)
        imageView.layer.addSublayer(frameSublayer)
    }
    
    func addTextView() {
        textView.frame = CGRect(x: 40, y: 240, width: screenWidth - 80, height: 200)
        view.addSubview(textView)
    }
    
    @objc func buttonAction() {
        if UIImagePickerController.isSourceTypeAvailable(.camera) {
            presentImagePickerController(withSourceType: .camera)
        } else {
            AVCaptureDevice.requestAccess(for: .video) { success in
                if success { // if request is granted (success is true)
                    self.presentImagePickerController(withSourceType: .camera)
                } else { // if request is denied (success is false)
                    // Create Alert
                    let alert = UIAlertController(title: "Camera Not Available", message: "Camera access is absolutely necessary to use this app", preferredStyle: .alert)
                    
                    alert.addAction(UIAlertAction(title: "OK", style: .default, handler: nil))
                    // Show the alert with animation
                    self.present(alert, animated: true)
                }
            }
        }
    }
    
    private func removeFrames() {
        guard let sublayers = frameSublayer.sublayers else { return }
        for sublayer in sublayers {
            sublayer.removeFromSuperlayer()
        }
    }
    
    // 1
    private func drawFeatures(in imageView: UIImageView, completion: (() -> Void)? = nil) {
        removeFrames()
        processor.process(in: imageView) { text, elements in
            elements.forEach() { element in
                self.frameSublayer.addSublayer(element.shapeLayer)
            }
            self.scannedText = text
            completion?()
        }
    }
}

extension ScannerViewController: UIImagePickerControllerDelegate, UINavigationControllerDelegate, UIPopoverPresentationControllerDelegate {
    // MARK: UIImagePickerController
    
    private func presentImagePickerController(withSourceType sourceType: UIImagePickerController.SourceType) {
        let controller = UIImagePickerController()
        controller.delegate = self
        controller.sourceType = sourceType
        controller.mediaTypes = [String(kUTTypeImage), String(kUTTypeMovie)]
        present(controller, animated: true, completion: nil)
    }
    
    // MARK: UIImagePickerController Delegate
    
    func imagePickerController(_ picker: UIImagePickerController, didFinishPickingMediaWithInfo info: [UIImagePickerController.InfoKey: Any]) {
        if let pickedImage =
            info[UIImagePickerController.InfoKey.originalImage] as? UIImage {
            
            imageView.contentMode = .scaleAspectFit
            let fixedImage = pickedImage.fixOrientation()
            imageView.image = fixedImage
            drawFeatures(in: imageView)
        }
        dismiss(animated: true, completion: nil)
    }
}
