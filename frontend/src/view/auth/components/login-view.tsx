import { Button } from '@/components/ui/button'
import {
  Card,
  CardContent,
  CardDescription,
  CardHeader,
  CardTitle,
} from '@/components/ui/card'
import {
  Field,
  FieldDescription,
  FieldGroup,
  FieldLabel,
} from '@/components/ui/field'
import { Input } from '@/components/ui/input'
import { cn } from '@/lib/utils'
import { useAuthToggle } from '../context'
import { PasswordInput } from './form-input'

import { LoadingOverlay } from '@/components/custom/overlay'
import { toast } from '@/providers/toast/toast-store'
import { zodResolver } from '@hookform/resolvers/zod'
import { useState } from 'react'
import { useForm } from 'react-hook-form'
import { z } from 'zod'

const loginSchema = z.object({
  email: z.string().email('Invalid email address'),
  password: z.string().min(6, 'Password must be at least 6 characters'),
})

type LoginFormValues = z.infer<typeof loginSchema>

function LoginView({ className, ...props }: React.ComponentProps<'div'>) {
  const { toggle } = useAuthToggle()
  const [loading, setLoading] = useState(false)

  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<LoginFormValues>({
    resolver: zodResolver(loginSchema),
  })

  const onSubmit: React.FormEventHandler<HTMLFormElement> = handleSubmit(
    async (values) => {
      setLoading(true)

      // Simulate API latency
      await new Promise((res) => setTimeout(res, 2000))

      const success = Math.random() > 0.5 // random success or failure

      if (success) {
        console.log('LOGIN SUCCESS:', values)
        toast({
          message: 'Login successful!',
          type: 'SUCCESS',
        })
      } else {
        toast({
          message: 'Failed to login — try again',
          type: 'ERROR',
        })
      }

      setLoading(false)
    }
  )

  return (
    <>
      <LoadingOverlay show={loading} />
      <div className={cn('flex flex-col gap-6', className)} {...props}>
        <Card>
          <CardHeader>
            <CardTitle>Login to your account</CardTitle>
            <CardDescription>
              Enter your email below to login to your account
            </CardDescription>
          </CardHeader>

          <CardContent>
            <form noValidate onSubmit={onSubmit}>
              <FieldGroup>
                {/* EMAIL FIELD */}
                <Field>
                  <FieldLabel htmlFor='email'>Email</FieldLabel>
                  <Input
                    id='email'
                    type='email'
                    placeholder='m@example.com'
                    className={errors.email ? 'border-red-500' : ''}
                    {...register('email')}
                  />
                  {errors.email && (
                    <p className='text-red-500 text-sm mt-1'>
                      {errors.email.message}
                    </p>
                  )}
                </Field>

                {/* PASSWORD FIELD */}
                <PasswordInput
                  {...register('password')}
                  error={errors.password}
                />

                {/* SUBMIT BUTTON */}
                <Field>
                  <Button type='submit' disabled={loading}>
                    {loading ? 'Logging in…' : 'Login'}
                  </Button>

                  <FieldDescription className='text-center'>
                    Don&apos;t have an account?{' '}
                    <a onClick={toggle} href='#'>
                      Sign up
                    </a>
                  </FieldDescription>
                </Field>
              </FieldGroup>
            </form>
          </CardContent>
        </Card>
      </div>
    </>
  )
}

export default LoginView
